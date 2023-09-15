package com.example.dm.security.jwt;

import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.AuthException;
import com.example.dm.security.PermitUrlProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {
  private final TokenProvider tokenProvider;
  private final PermitUrlProperties urlProperties;

  @Value("${jwt.access.header}")
  private String ACCESS_HEADER;

  @Value("${jwt.refresh.header}")
  private String REFRESH_HEADER;


  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {
    if(doFilter(request, response, filterChain)) return;

    String accessToken = resolveToken(request.getHeader(ACCESS_HEADER));
    String refreshToken = resolveToken(request.getHeader(REFRESH_HEADER));

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

    setSecurityContextHolder(accessToken);
    tokenProvider.setAuthenticationTokens(response, accessToken, refreshToken);
  }


  private boolean doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    boolean b = false;
    String method = request.getMethod();
    String url = request.getRequestURI();
    if (("GET".equals(method) && urlProperties.getGet().contains(url))
        || ("POST".equals(method) && urlProperties.getPost().contains(url))) {
      filterChain.doFilter(request, response);
      b=true;
    }
    return b;
  }

  private String resolveToken(String bearerToken) {
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public void setSecurityContextHolder(String token) {
    Authentication authentication = tokenProvider.getAuthentication(token);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}