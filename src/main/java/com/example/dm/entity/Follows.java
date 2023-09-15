package com.example.dm.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "follows")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follows extends DeletedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id")
    private UserProfiles userProfiles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_profile_id")
    private UserProfiles targetUserProfiles;

    public static Follows create(UserProfiles userProfiles, UserProfiles targetUserProfiles) {
        return Follows.builder()
                .userProfiles(userProfiles)
                .targetUserProfiles(targetUserProfiles)
                .build();
    }
}
