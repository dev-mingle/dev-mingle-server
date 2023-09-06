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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  
  @Column(length = 50, unique = true)
  private String email;
  private String password;
  
  private String provider;
  private String providerId;
  
  @Column(length = 20)
  private String role;

  @ColumnDefault("true")
  private boolean isVerificated;
  @ColumnDefault("false")
  private boolean isBlocked = false;
  @ColumnDefault("false")
  private boolean isInactivated = false;
  @ColumnDefault("false")
  private boolean isDeleted = false;

  private LocalDateTime deletedAt;
  private LocalDateTime passwordChangedAt;

  @JsonIgnore
  @OneToOne(mappedBy = "users", cascade = CascadeType.ALL)
  private UserProfiles userProfiles;

  public static Users create(String email, String password, String provider, String providerId){
    Users users = new Users();
    users.setEmail(email);
    users.setPassword(password);
    users.setProvider(provider);
    users.setProviderId(providerId);
    users.setRole("USER");
    return users;
  }
}
