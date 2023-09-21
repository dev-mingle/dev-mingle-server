package com.example.dm.controller;

import com.example.dm.dto.ApiResponse;
import com.example.dm.dto.form.MypageForm;
import com.example.dm.dto.form.SignupUserProfilesData;
import com.example.dm.entity.LoginUser;
import com.example.dm.entity.UserProfiles;
import com.example.dm.entity.Users;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.AuthException;
import com.example.dm.repository.UserProfileRepository;
import com.example.dm.repository.UsersRepository;
import com.example.dm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path.default}/users")
public class UserController extends BaseController {
  private final UserService userService;
  private final UsersRepository usersRepository;
  private final UserProfileRepository userProfileRepository;

  /* 회원정보 조회 */
  @GetMapping("/profile")
  public ResponseEntity<ApiResponse> getProfiles(@AuthenticationPrincipal LoginUser loginUser){
    UserProfiles userProfiles = userProfileRepository.findByUsers_IdAndIsDeletedIsFalse(loginUser.getId()).orElseThrow(
        () -> new AuthException(ApiResultStatus.USER_NOT_FOUND)
    );

    return responseBuilder(userService.setUserProfileData(userProfiles, loginUser), HttpStatus.OK);
  }

  /* 회원정보 수정 */
  @PutMapping("/profile")
  public ResponseEntity<ApiResponse> getProfiles(@AuthenticationPrincipal LoginUser loginUser,
                                                 @RequestBody MypageForm mypageForm){
    UserProfiles userProfiles = userProfileRepository.findByUsers_IdAndIsDeletedIsFalse(loginUser.getId()).orElseThrow(
        () -> new AuthException(ApiResultStatus.USER_NOT_FOUND)
    );

    userProfiles.setCity(mypageForm.getCity());
    userProfiles.setState(mypageForm.getState());
    userProfiles.setStreet(mypageForm.getStreet());
    userProfiles.setLatitude(mypageForm.getLatitude());
    userProfiles.setLongitude(mypageForm.getLongitude());
    userProfiles.setIntroduce(mypageForm.getIntroduce());
    userProfiles.setUrl(mypageForm.getUrl());
    userProfiles.setNickname(mypageForm.getNickname());

    return responseBuilder(userService.setUserProfileData(userProfiles, loginUser), HttpStatus.OK);
  }

  /* 회원탈퇴 */
  @DeleteMapping
  public ResponseEntity<ApiResponse> deleteUser(@AuthenticationPrincipal LoginUser loginUser) {
    String email = loginUser.getEmail();

    Users user = usersRepository.findByEmailAndIsDeletedIsFalse(email).orElseThrow();
    user.delete();
    usersRepository.save(user);

    UserProfiles userProfiles = userProfileRepository.findByUsers_IdAndIsDeletedIsFalse(user.getId()).orElseThrow();
    userProfiles.delete();
    userProfileRepository.save(userProfiles);

    return responseBuilder(email, HttpStatus.OK);
  }
}
