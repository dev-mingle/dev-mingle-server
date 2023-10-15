package com.example.dm.entity;

import com.example.dm.dto.users.SignupDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfiles extends DeletedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Images images;

    @Column(length = 20)
    private String nickname;

    @Column(length = 20)
    private String city;
    @Column(length = 20)
    private String state;
    @Column(length = 20)
    private String street;

    private Double latitude;
    private Double longitude;

    private String introduce;
    private String url;
    @Column(length = 50)
    private String urlName;

    @JsonIgnore
    @OneToMany(mappedBy = "userProfiles", cascade = CascadeType.ALL)
    private List<Follows> followsByUsers;

    @JsonIgnore
    @OneToMany(mappedBy = "targetUserProfiles", cascade = CascadeType.ALL)
    private List<Follows> followsByTargetUsers;

    @OneToMany(mappedBy = "userProfiles")
    private List<ChatMembers> chatMembers = new ArrayList<>();

    public static UserProfiles create(Users users, SignupDto signupDto) {
        UserProfiles userProfiles = new UserProfiles();
        userProfiles.setUsers(users);
        userProfiles.setNickname(signupDto.getNickname());
        userProfiles.setCity(signupDto.getCity());
        userProfiles.setState(signupDto.getState());
        userProfiles.setStreet(signupDto.getStreet());
        userProfiles.setLatitude(signupDto.getLatitude());
        userProfiles.setLongitude(signupDto.getLongitude());
        userProfiles.setIntroduce(signupDto.getIntroduce());
        userProfiles.setUrl(signupDto.getUrl());
        userProfiles.setUrlName(signupDto.getUrlName());
        return userProfiles;
    }
}
