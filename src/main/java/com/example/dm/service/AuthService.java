package com.example.dm.service;

import com.example.dm.entity.LoginUser;
import com.example.dm.entity.UserProfiles;
import com.example.dm.entity.Users;
import com.example.dm.repository.UserProfileRepository;
import com.example.dm.repository.UsersRepository;
import com.example.dm.security.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
  private final UsersRepository usersRepository;
  private final UserProfileRepository userProfileRepository;
  private final TokenProvider tokenProvider;

  @Override
  public LoginUser loadUserByUsername(String username) throws UsernameNotFoundException {
    Users users = usersRepository.findByEmailAndIsDeletedIsFalse(username).orElseThrow();
    UserProfiles userProfiles = userProfileRepository.findByUsers_IdAndIsDeletedIsFalse(users.getId()).orElseThrow();
    return LoginUser.create(users.getId(), userProfiles.getId(), users.getEmail(),
        users.getPassword(), users.getRole(), userProfiles.getNickname());
  }

  /* token context 설정 (유저정보 변경시에도 적용) */
  public void setAuthentication(HttpServletResponse response, LoginUser loginUser) {
    String accessToken="", refreshToken="";
    if(loginUser!=null){
      accessToken = tokenProvider.generateAccessToken(loginUser);
      refreshToken = tokenProvider.generateRefreshToken(loginUser);
    }
    tokenProvider.setAuthenticationTokens(response, accessToken, refreshToken);
  }
}
