package com.example.dm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginUser implements UserDetails {
  @Id
  private Long id;
  //private Long userProfileId;
  private String email;
  private String password;
  private String role;
  private String nickname;

  public static LoginUser create(Long id, String email, String password, String role, String nickname){
    LoginUser user = new LoginUser();
    user.setId(id);
    //user.setUserProfileId(userProfileId);
    user.setEmail(email);
    user.setPassword(password);
    user.setRole("ROLE_"+role);
    user.setNickname(nickname);
    return user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
    grantedAuthorities.add(new SimpleGrantedAuthority(role));
    return grantedAuthorities;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
