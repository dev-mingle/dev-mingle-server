package com.example.dm.security.jwt;

import com.example.dm.entity.LoginUser;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenProvider {
  private final Key key;

  @Value("${jwt.secretKey}")
  private String secretKey;

  @Value("${jwt.access.tokenValidTime}")
  private long accessTokenValidTime;

  @Value("${jwt.access.header}")
  private String accessHeader;

  @Value("${jwt.refresh.tokenValidTime}")
  private long refreshTokenValidTime;

  @Value("${jwt.refresh.header}")
  private String refreshHeader;

  public TokenProvider(@Value("${jwt.secretKey}") String secretKey) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  /* access token */
  public String generateAccessToken(String token){
    return buildToken(getExtraClaims(token), extractUsername(token), accessTokenValidTime);
  }

  public String generateAccessToken(LoginUser loginUser){
    return buildToken(getExtraClaims(loginUser), loginUser.getUsername(), accessTokenValidTime);
  }

  /* refresh token */
  public String generateRefreshToken(String token){
    return buildToken(getExtraClaims(token), extractUsername(token), refreshTokenValidTime);
  }

  public String generateRefreshToken(LoginUser loginUser){
    return buildToken(getExtraClaims(loginUser), loginUser.getUsername(), accessTokenValidTime);
  }


  /* getExtraClaims */
  public Map<String,Object> getExtraClaims(String token){
    Map<String,Object> extraClaims = new HashMap<>();
    Claims claims = parseClaims(token);
    extraClaims.put("id", extractClaim(token, (Function<Claims, String>) claims.get("id")));
    extraClaims.put("password", extractClaim(token, (Function<Claims, String>) claims.get("password")));
    extraClaims.put("role", extractClaim(token, (Function<Claims, String>) claims.get("role")));
    extraClaims.put("nickname", extractClaim(token, (Function<Claims, String>) claims.get("nickname")));
    return extraClaims;
  }

  public Map<String,Object> getExtraClaims(LoginUser loginUser){
    Map<String,Object> extraClaims = new HashMap<>();
    extraClaims.put("id", loginUser.getId());
    extraClaims.put("password", loginUser.getPassword());
    extraClaims.put("role", loginUser.getRole());
    extraClaims.put("nickname", loginUser.getNickname());
    return extraClaims;
  }


  public Map<String,String> rebuildToken(String refreshToken) {
    String accessToken = generateAccessToken(refreshToken);
    refreshToken = generateRefreshToken(refreshToken);

    Map<String,String> map = new HashMap<>();
    map.put("accessToken", accessToken);
    map.put("refreshToken", refreshToken);
    return map;
  }

  private String buildToken(Map<String, Object> extraClaims, String username, long tokenValidTime) {
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public void setAuthenticationTokens(HttpServletResponse response, String accessToken, String refreshToken){
    response.setHeader(accessHeader, accessToken);
    response.setHeader(refreshHeader, refreshToken);
  }

  public Authentication getAuthentication(String accessToken) {
    Claims claims = extractAllClaims(accessToken);

    if (claims.get("role") == null) {
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get("role").toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    // role 한 개로 적용
    LoginUser loginUser = LoginUser.create(
        Long.parseLong(claims.get("id").toString()), claims.getSubject(), claims.get("password").toString(),
        claims.get("role").toString(), claims.get("nickname").toString());
    return new UsernamePasswordAuthenticationToken(loginUser, "", authorities);
  }

  /* 토큰 만료여부 체크 */
  public boolean expiredToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return false;
    } catch (ExpiredJwtException e) {
      return true;
    }
  }

  /* 토큰 유효성 체크 */
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      log.info("유효하지 않은 JWT 토큰입니다.", e);
      throw new AuthException(ApiResultStatus.TOKEN_INVALID);
    } catch (ExpiredJwtException e) {
      log.info("만료된 JWT 토큰입니다.", e);
      throw new AuthException(ApiResultStatus.TOKEN_DATE_EXPIRED);
    } catch (UnsupportedJwtException e) {
      log.info("지원하지 않는 JWT 토큰입니다.", e);
      throw new AuthException(ApiResultStatus.TOKEN_INVALID);
    } catch (IllegalArgumentException e) {
      log.info("JWT 토큰 값(claims)가 없습니다.", e);
      throw new AuthException(ApiResultStatus.TOKEN_INVALID);
    }
    return true;
  }

  // email
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    return claimsResolver.apply(extractAllClaims(token));
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}