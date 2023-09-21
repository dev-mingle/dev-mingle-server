package com.example.dm.service;

import com.example.dm.entity.LoginUser;
import com.example.dm.entity.UserProfiles;
import com.example.dm.entity.Users;
import com.example.dm.repository.UserProfileRepository;
import com.example.dm.repository.UsersRepository;
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

  @Override
  public LoginUser loadUserByUsername(String username) throws UsernameNotFoundException {
    Users users = usersRepository.findByEmailAndIsDeletedIsFalse(username).orElseThrow();
    UserProfiles userProfiles = userProfileRepository.findByUsers_IdAndIsDeletedIsFalse(users.getId()).orElseThrow();
    return LoginUser.create(users.getId(), users.getEmail(), users.getPassword(), users.getRole(), userProfiles.getNickname());
  }
}
