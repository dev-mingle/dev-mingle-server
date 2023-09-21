package com.example.dm.service;

import com.example.dm.dto.users.SignupUserResponse;
import com.example.dm.dto.users.SignupUserProfilesResponse;
import com.example.dm.dto.users.UserProfileResponse;
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
  public SignupUserResponse setSignupUserData(UserProfiles userProfiles, Users users) {
    SignupUserResponse signupUserResponse = SignupUserResponse.builder()
        .email(users.getEmail())
        .createdAt(users.getCreatedAt())
        .userProfile(setSignupUserProfilesData(userProfiles))
        .build();
    return signupUserResponse;
  }

  public UserProfileResponse setUserProfileData(UserProfiles userProfiles, LoginUser loginUser) {
    UserProfileResponse userProfileResponse = UserProfileResponse.builder()
        .email(loginUser.getEmail())
        .createdAt(userProfiles.getCreatedAt())
        .updatedAt(userProfiles.getUpdatedAt())
        .userProfile(setSignupUserProfilesData(userProfiles))
        .build();
    return userProfileResponse;
  }

  public SignupUserProfilesResponse setSignupUserProfilesData(UserProfiles userProfiles) {
    SignupUserProfilesResponse signupUserProfilesResponse = SignupUserProfilesResponse.builder()
        .nickname(userProfiles.getNickname())
        .city(userProfiles.getCity())
        .state(userProfiles.getState())
        .street(userProfiles.getStreet())
        .introduce(userProfiles.getIntroduce())
        .url(userProfiles.getUrl())
        .urlName(userProfiles.getUrlName())
        .build();
    return signupUserProfilesResponse;
  }
}
