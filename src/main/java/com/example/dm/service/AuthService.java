package com.example.dm.service;

import com.example.dm.entity.LoginUser;
import com.example.dm.entity.Users;
import com.example.dm.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
  private final UsersRepository usersRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Users users = usersRepository.findByEmailAndIsDeletedIsFalse(username).orElseThrow();
    return LoginUser.create(users.getId(), users.getEmail(), users.getPassword(), users.getRole());
  }
}
