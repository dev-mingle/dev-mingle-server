package com.example.dm.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import com.example.dm.entity.LoginUser;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("TokenProvider 메서드 테스트")
class TokenProviderTest {
  @Autowired
  private TokenProvider tokenProvider;

  @Value("${jwt.access.header}")
  private String accessHeader;
  @Value("${jwt.refresh.header}")
  private String refreshHeader;

  @Mock
  private HttpServletResponse response;
  @Mock
  private Jwts jwts;
  @Mock
  private Key key;

  Map<String,Object> map;

  private LoginUser loginUser;
  private final Long ID = 1L;
  private final Long USERPROFILES_ID = 1L;
  private final String EMAIL = "token@test.com";
  private final String PASSWORD = "password";
  private final String ROLE = "USER";
  private final String NICKNAME = "nickname";

  @BeforeEach
  void setUp(){
    loginUser = LoginUser.create(ID, USERPROFILES_ID,EMAIL, PASSWORD, ROLE, NICKNAME);
  }

  @Test
  @DisplayName("토큰 생성시 User 정보가 들어가는지 확인")
  void generateToken() {
    // LoginUser 정보로 생성하는 token
    String accessTokenByUser = tokenProvider.generateAccessToken(loginUser);
    String refreshTokenByUser = tokenProvider.generateRefreshToken(loginUser);
    assertEquals(EMAIL, tokenProvider.extractUsername(accessTokenByUser));
    assertEquals(EMAIL, tokenProvider.extractUsername(refreshTokenByUser));

    // token 정보로 생성하는 token
    String newAccessToken = tokenProvider.generateAccessToken(accessTokenByUser);
    String newRefreshToken = tokenProvider.generateRefreshToken(refreshTokenByUser);
    assertEquals(
        tokenProvider.getExtraClaims(newAccessToken).get("password"),
        tokenProvider.getExtraClaims(loginUser).get("password")
    );

    assertEquals(
        tokenProvider.getExtraClaims(newRefreshToken).get("password"),
        tokenProvider.getExtraClaims(loginUser).get("password")
    );
  }

  @Test
  @DisplayName("claims 추출 확인")
  void extraClaims() {
    String token = tokenProvider.generateAccessToken(loginUser);
    map = tokenProvider.getExtraClaims(token);
    assertEquals(ID, Long.valueOf((Integer) map.get("id")));
    assertEquals(PASSWORD, map.get("password"));
    assertEquals("ROLE_" + ROLE, map.get("role"));
    assertEquals(NICKNAME, map.get("nickname"));
    assertEquals(EMAIL, tokenProvider.extractUsername(token));
  }

  @Test
  @DisplayName("response에 인증정보 넣기")
  void setAuthenticationTokens() {
    String accessToken = tokenProvider.generateAccessToken(loginUser);
    String refreshToken = tokenProvider.generateRefreshToken(loginUser);
    tokenProvider.setAuthenticationTokens(response, accessToken, refreshToken);
    verify(response).setHeader(accessHeader, accessToken);
    verify(response).setHeader(refreshHeader, refreshToken);
  }

  @Nested
  @DisplayName("토큰 유효성 테스트")
  class validateToken {
    JwtParser jwtParser = jwts.parserBuilder().setSigningKey(key).build();

    @Test
    @DisplayName("유효한 토큰 여부")
    void validToken(){
      String token = tokenProvider.generateAccessToken(loginUser);
      assertTrue(tokenProvider.validateToken(token));
    }

    @Test
    @DisplayName("유효하지 않은 토큰 여부")
    void invalidToken(){
      assertThrows(IllegalArgumentException.class, () -> jwtParser.parseClaimsJws(""));
    }

    @Test
    @DisplayName("지원하지 않는 토큰 여부")
    void unsupportedToken(){
      String unsupportedToken = "eyJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IntiY3J5cHR9JDJhJDEwJFJDbE1pVDlUS2NsNmRHN2ZxT2tJdHVCdzNqemJKYzZNQkgwWkV5bExTRFdLcGxkN3FGWlg2Iiwicm9sZSI6IlJPTEVfVVNFUiIsIm5pY2tuYW1lIjoibmlja25hbWUiLCJpZCI6MSwic3ViIjoiZW1haWxAZG9tYWluLmNvbSIsImlhdCI6MTY5NTk4NTI1MSwiZXhwIjoxNjk1OTg1NTUxfQ.Lh9rSa6O_oW__FThK6iwPPFUNKNW5S60_mEYqGdvI7A";
      assertThrows(UnsupportedJwtException.class, () -> jwtParser.parseClaimsJws(unsupportedToken));
    }

//    @Test
//    @DisplayName("만료된 토큰 여부")
//    void expiredToken(){
//      String expiredToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjoxNTE2MjM5MDIzfQ.4r7wFehZ2HeVSULgOCTzzH7Qaru2TndAh-TDJ2YXJts";
//      assertThrows(ExpiredJwtException.class, () -> jwtParser.parseClaimsJws(expiredToken));
//    }
  }
}