package com.example.dm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends DeletedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String email;
    private String password;

    private String provider;
    private String providerId;

    @Column(length = 20)
    private String role;

    @ColumnDefault("true")
    private boolean isVerified;
    @ColumnDefault("false")
    private boolean isBlocked;
    @ColumnDefault("false")
    private boolean isInactivated;

    private LocalDateTime passwordChangedAt;

    @JsonIgnore
    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL)
    private UserProfiles userProfiles;

    public static Users create(String email, String password, String provider, String providerId) {
        Users users = new Users();
        users.setEmail(email);
        users.setPassword(password);
        users.setProvider(provider);
        users.setProviderId(providerId);
        users.setRole("USER");
        return users;
    }
}
