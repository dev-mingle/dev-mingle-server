package com.example.dm.security.jwt;

import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
//@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {
  private final TokenProvider tokenProvider;

  @Value("${jwt.access.header}")
  private String accessHeader;

  @Value("${jwt.refresh.header}")
  private String refreshHeader;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) {
    String accessToken = resolveToken(request.getHeader(accessHeader));
    String refreshToken = resolveToken(request.getHeader(refreshHeader));

    if(accessToken==null || refreshToken==null){
      throw new AuthException(ApiResultStatus.TOKEN_NOT_FOUND);
    }

    tokenProvider.validateToken(accessToken);
    if(tokenProvider.expiredToken(accessToken) && !tokenProvider.expiredToken(refreshToken)){
      tokenProvider.validateToken(refreshToken);
      Map<String,String> map = tokenProvider.rebuildToken(refreshToken);
      accessToken = map.get("accessToken");
      refreshToken = map.get("refreshToken");
    }

    tokenProvider.setAuthenticationTokens(response, accessToken, refreshToken);
  }

  private String resolveToken(String bearerToken) {
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}