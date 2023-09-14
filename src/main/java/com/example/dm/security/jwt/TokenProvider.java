package com.example.dm.security.jwt;

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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
  public String generateAccessToken(UserDetails userDetails){
    Map<String,Object> extraClaims = new HashMap<>();
    extraClaims.put("role", userDetails.getAuthorities());
    return buildToken(extraClaims, userDetails.getUsername(), accessTokenValidTime);
  }

  public String generateAccessToken(Collection<? extends GrantedAuthority> roles, String username) {
    Map<String,Object> extraClaims = new HashMap<>();
    extraClaims.put("role", roles);
    return buildToken(extraClaims, username, accessTokenValidTime);
  }

  /* refresh token */
  public String generateRefreshToken(UserDetails userDetails){
    Map<String,Object> extraClaims = new HashMap<>();
    extraClaims.put("role", userDetails.getAuthorities());
    return buildToken(extraClaims, userDetails.getUsername(), refreshTokenValidTime);
  }

  public String generateRefreshToken(Collection<? extends GrantedAuthority> roles, String username) {
    Map<String,Object> extraClaims = new HashMap<>();
    extraClaims.put("role", roles);
    return buildToken(extraClaims, username, accessTokenValidTime);
  }

  public Map<String,String> rebuildToken(String refreshToken) {
    String username = extractUsername(refreshToken);
    Collection<? extends GrantedAuthority> roles = extractRoles(refreshToken);

    String accessToken = generateAccessToken(roles, username);
    refreshToken = generateRefreshToken(roles, username);

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
    Claims claims = parseClaims(accessToken);

    if (claims.get("role") == null) {
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get("role").toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    UserDetails principal = new User(claims.getSubject(), "", authorities);
    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
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
      throw new AuthException(ApiResultStatus.TOKEN_INVALID.getMessage());
      //log.info("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      throw new AuthException(ApiResultStatus.TOKEN_DATE_EXPIRED.getMessage());
      //log.info("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
      throw new AuthException(ApiResultStatus.TOKEN_INVALID.getMessage());
      //log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      throw new AuthException(ApiResultStatus.TOKEN_INVALID.getMessage());
      //log.info("JWT claims string is empty.", e);
    }
    return true;
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Collection<? extends GrantedAuthority> extractRoles(String token) {
    Claims claims = parseClaims(token);
    return extractClaim(token, (Function<Claims, Collection<? extends GrantedAuthority>>) claims.get("role"));
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