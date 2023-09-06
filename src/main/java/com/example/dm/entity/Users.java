package com.example.dm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  
  @Column(length = 50, unique = true)
  private String email;
  private String password;
  
  private String provider;
  private String provider_id;
  
  @Column(length = 20)
  private String role;
  
  @Builder.Default
  private boolean is_verificated = true;
  @Builder.Default
  private boolean is_blocked = false;
  @Builder.Default
  private boolean is_inactivated = false;
  @Builder.Default
  private boolean is_deleted = false;

  private LocalDateTime deleted_at;
  private LocalDateTime password_changed_at;

  @JsonIgnore
  @OneToOne(mappedBy = "users", cascade = CascadeType.ALL)
  private UserProfiles userProfiles;

  public static Users create(String email, String password, String provider, String provider_id){
    Users users = new Users();
    users.setEmail(email);
    users.setPassword(password);
    users.setProvider(provider);
    users.setProvider_id(provider_id);
    users.setRole("USER");
    return users;
  }
}
