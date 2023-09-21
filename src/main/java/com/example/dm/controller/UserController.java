package com.example.dm.controller;

import com.example.dm.dto.ApiResponse;
import com.example.dm.dto.form.MypageForm;
import com.example.dm.entity.LoginUser;
import com.example.dm.entity.UserProfiles;
import com.example.dm.entity.Users;
import com.example.dm.enums.MailType;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.AuthException;
import com.example.dm.repository.UserProfileRepository;
import com.example.dm.repository.UsersRepository;
import com.example.dm.service.UserService;
import com.example.dm.util.MailSender;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
  private final MailSender mailSender;
  private final PasswordEncoder passwordEncoder;

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

    userService.nicknameConfirm(mypageForm.getNickname());

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

  /* 비밀번호 초기화 */
  @PostMapping("/password/reset")
  public ResponseEntity<ApiResponse> resetPassword(@AuthenticationPrincipal LoginUser loginUser,
                                                   @RequestBody JsonNode jsonNode){
    if(!passwordEncoder.matches(jsonNode.get("password").asText(), loginUser.getPassword())){
      throw new AuthException(ApiResultStatus.WRONG_PASSWORD);
    }

    try {
      mailSender.sendEmail(loginUser.getEmail(), MailType.RandomPassword);
    } catch (MessagingException e) {
      throw new RuntimeException(e);  // temp
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);  // temp
    }
    return responseBuilder(loginUser.getEmail(), HttpStatus.OK);
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
