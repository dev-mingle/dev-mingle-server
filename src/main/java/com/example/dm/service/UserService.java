package com.example.dm.service;

import com.example.dm.dto.form.SignupUserData;
import com.example.dm.dto.form.SignupUserProfilesData;
import com.example.dm.dto.form.UserProfileData;
import com.example.dm.entity.LoginUser;
import com.example.dm.entity.UserProfiles;
import com.example.dm.entity.Users;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.AuthException;
import com.example.dm.repository.UserProfileRepository;
import com.example.dm.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
  private final UsersRepository usersRepository;
  private final UserProfileRepository userProfileRepository;

  /* 이메일 가입여부 */
  public void emailConfirm(String email) {
    if(usersRepository.countByEmailAndIsDeletedIsFalse(email)!=0)
      throw new AuthException(ApiResultStatus.ALREADY_SIGNED_UP);
  }

  /* 닉네임 중복여부 */
  public void nicknameConfirm(String nickname) {
    if(userProfileRepository.countByNicknameAndIsDeletedIsFalse(nickname)!=0)
      throw new AuthException(ApiResultStatus.ALREADY_SIGNED_UP_NICKNAME);
  }
  public SignupUserData setSignupUserData(UserProfiles userProfiles, Users users) {
    SignupUserData signupUserData = SignupUserData.builder()
        .email(users.getEmail())
        .createdAt(users.getCreatedAt())
        .userProfile(setSignupUserProfilesData(userProfiles))
        .build();
    return signupUserData;
  }

  public UserProfileData setUserProfileData(UserProfiles userProfiles, LoginUser loginUser) {
    UserProfileData userProfileData = UserProfileData.builder()
        .email(loginUser.getEmail())
        .createdAt(userProfiles.getCreatedAt())
        .updatedAt(userProfiles.getUpdatedAt())
        .userProfile(setSignupUserProfilesData(userProfiles))
        .build();
    return userProfileData;
  }

  public SignupUserProfilesData setSignupUserProfilesData(UserProfiles userProfiles) {
    SignupUserProfilesData signupUserProfilesData = SignupUserProfilesData.builder()
        .nickname(userProfiles.getNickname())
        .city(userProfiles.getCity())
        .state(userProfiles.getState())
        .street(userProfiles.getStreet())
        .introduce(userProfiles.getIntroduce())
        .url(userProfiles.getUrl())
        .urlName(userProfiles.getUrlName())
        .build();
    return signupUserProfilesData;
  }
}
