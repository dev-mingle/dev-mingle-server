package com.example.dm.service;

import com.example.dm.dto.form.SignupUserData;
import com.example.dm.dto.form.SignupUserProfilesData;
import com.example.dm.entity.UserProfiles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
  public SignupUserData setUserData(UserProfiles userProfiles) {
    SignupUserProfilesData signupUserProfilesData = SignupUserProfilesData.builder()
        .nickname(userProfiles.getNickname())
        .city(userProfiles.getCity())
        .state(userProfiles.getState())
        .street(userProfiles.getStreet())
        .introduce(userProfiles.getIntroduce())
        .url(userProfiles.getUrl())
        .urlName(userProfiles.getUrlName())
        .build();

    SignupUserData signupUserData = SignupUserData.builder()
        .email(userProfiles.getUsers().getEmail())
        .createdAt(userProfiles.getUsers().getCreatedAt())
        .userProfile(signupUserProfilesData)
        .build();

    return signupUserData;
  }
}
