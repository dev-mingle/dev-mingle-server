package com.example.dm.entity;

import com.example.dm.dto.form.SignupForm;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfiles extends DeletedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Users users;

    private Long imageId;

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

    public static UserProfiles create(Users users, SignupForm signupForm) {
        UserProfiles userProfiles = new UserProfiles();
        userProfiles.setUsers(users);
        userProfiles.setNickname(signupForm.getNickname());
        userProfiles.setCity(signupForm.getCity());
        userProfiles.setState(signupForm.getState());
        userProfiles.setStreet(signupForm.getStreet());
        userProfiles.setLatitude(signupForm.getLatitude());
        userProfiles.setLongitude(signupForm.getLongitude());
        userProfiles.setIntroduce(signupForm.getIntroduce());
        userProfiles.setUrl(signupForm.getUrl());
        userProfiles.setUrlName(signupForm.getUrlName());
        return userProfiles;
    }
}
