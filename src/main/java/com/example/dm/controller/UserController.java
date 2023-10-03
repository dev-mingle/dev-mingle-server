package com.example.dm.controller;

import com.example.dm.dto.ApiResponse;
import com.example.dm.dto.users.ChangePwdDto;
import com.example.dm.dto.users.MypageDto;
import com.example.dm.entity.LoginUser;
import com.example.dm.entity.UserProfiles;
import com.example.dm.entity.Users;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.AuthException;
import com.example.dm.repository.UserProfileRepository;
import com.example.dm.repository.UsersRepository;
import com.example.dm.service.AuthService;
import com.example.dm.service.UserService;
import com.example.dm.util.MailSender;
import com.example.dm.util.PasswordGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
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
  private final AuthService authService;
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
  public ResponseEntity<ApiResponse> editProfiles(@AuthenticationPrincipal LoginUser loginUser,
                                                 @RequestBody MypageDto mypageDto,
                                                 HttpServletResponse response){
    UserProfiles userProfiles = userProfileRepository.findByUsers_IdAndIsDeletedIsFalse(loginUser.getId()).orElseThrow(
        () -> new AuthException(ApiResultStatus.USER_NOT_FOUND)
    );

    userService.nicknameConfirm(mypageDto.getNickname());

    userProfiles.setCity(mypageDto.getCity());
    userProfiles.setState(mypageDto.getState());
    userProfiles.setStreet(mypageDto.getStreet());
    userProfiles.setLatitude(mypageDto.getLatitude());
    userProfiles.setLongitude(mypageDto.getLongitude());
    userProfiles.setIntroduce(mypageDto.getIntroduce());
    userProfiles.setUrl(mypageDto.getUrl());
    userProfiles.setNickname(mypageDto.getNickname());
    userProfileRepository.save(userProfiles);

    loginUser.setNickname(mypageDto.getNickname());
    authService.setAuthentication(response, loginUser);
    return responseBuilder(userService.setUserProfileData(userProfiles, loginUser), HttpStatus.OK);
  }

  /* 비밀번호 초기화 */
  @PostMapping("/password/reset")
  public ResponseEntity<ApiResponse> resetPassword(@AuthenticationPrincipal LoginUser loginUser,
                                                   @RequestBody JsonNode jsonNode,
                                                   HttpServletResponse response){
    if(!passwordEncoder.matches(jsonNode.get("password").asText(), loginUser.getPassword())){
      throw new AuthException(ApiResultStatus.WRONG_PASSWORD);
    }

    String randomPassword = PasswordGenerator.generatePassword();

    try {
      mailSender.sendRandomPwd(loginUser.getEmail(), randomPassword);
    } catch (MessagingException e) {
      throw new AuthException(ApiResultStatus.SEND_MAIL_FAILED);
    } catch (UnsupportedEncodingException e) {
      throw new AuthException(ApiResultStatus.ENCODING_ISSUE);
    }

    Users user = usersRepository.findById(loginUser.getId()).orElseThrow();
    String encodePassword = passwordEncoder.encode(randomPassword);
    user.setPassword(encodePassword);
    user.setPasswordChangedAt(LocalDateTime.now());
    user.setRandomPassword(true);
    usersRepository.save(user);

    loginUser.setPassword(encodePassword);
    authService.setAuthentication(response, loginUser);
    return responseBuilder(loginUser.getEmail(), HttpStatus.OK);
  }

  /* 비밀번호 변경 */
  @PostMapping("/password/change")
  public ResponseEntity<ApiResponse> changePassword(@AuthenticationPrincipal LoginUser loginUser,
                                                    @RequestBody ChangePwdDto changePwdDto,
                                                    HttpServletResponse response){
    String resetPassword = changePwdDto.getResetPassword();
    String newPassword = changePwdDto.getPassword();

    if(!passwordEncoder.matches(resetPassword, loginUser.getPassword())){
      throw new AuthException(ApiResultStatus.WRONG_PASSWORD);
    }

    Users user = usersRepository.findById(loginUser.getId()).orElseThrow();
    String encodePassword = passwordEncoder.encode(newPassword);
    user.setPassword(encodePassword);
    user.setPasswordChangedAt(LocalDateTime.now());
    user.setRandomPassword(false);
    usersRepository.save(user);

    loginUser.setPassword(encodePassword);
    authService.setAuthentication(response, loginUser);
    return responseBuilder(Boolean.TRUE, HttpStatus.OK);
  }

  /* 랜덤 비밀번호 발급여부 확인용 */
  @PostMapping("/password/reset/confirm")
  public ResponseEntity<ApiResponse> isRandomPassword(@AuthenticationPrincipal LoginUser loginUser){
    boolean isRandomPassword = usersRepository.findIsRandomPasswordById(loginUser.getId());
    return responseBuilder(isRandomPassword, HttpStatus.OK);
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
