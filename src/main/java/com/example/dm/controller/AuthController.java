package com.example.dm.controller;

import com.example.dm.dto.ApiResponse;
import com.example.dm.dto.form.SignupForm;
import com.example.dm.dto.form.SignupUserData;
import com.example.dm.dto.form.SignupUserProfilesData;
import com.example.dm.entity.UserProfiles;
import com.example.dm.entity.Users;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.BadRequestException;
import com.example.dm.repository.UserProfilesRepository;
import com.example.dm.repository.UsersRepository;
import com.example.dm.util.MailSender;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path.default}/users")
public class AuthController extends BaseController {
  private final UsersRepository usersRepository;
  private final UserProfilesRepository userProfilesRepository;
  private final MailSender mailSender;

  /* 이메일 인증발급 */
  @PostMapping("/otp")
  public ResponseEntity<ApiResponse> sendOtp(@RequestParam("email") String email) {
    if(!emailConfirm(email)){
      throw new BadRequestException(ApiResultStatus.ALREADY_SIGNED_UP.getMessage());
    }

    try {
      mailSender.sendOtp(email);
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return responseBuilder(email, HttpStatus.OK);
  }

  /* 회원가입 */
  @PostMapping
  public ResponseEntity<ApiResponse> signup(@Valid @RequestBody SignupForm signupForm) {
    if(!emailConfirm(signupForm.getEmail())){
      throw new BadRequestException(ApiResultStatus.ALREADY_SIGNED_UP.getMessage());
    }

    Users users = Users.create(signupForm.getEmail(),
                          signupForm.getPassword(),
                          signupForm.getProvider()==null? "EMAIL":signupForm.getProvider(),
                          signupForm.getProviderId()==null? "":signupForm.getProviderId());
    usersRepository.save(users);

    UserProfiles userProfiles = UserProfiles.create(users, signupForm);
    userProfilesRepository.save(userProfiles);

    SignupUserData signupUserData = SignupUserData.builder()
        .email(users.getEmail())
        .created_at(users.getCreatedAt())
        .user_profile(
            SignupUserProfilesData.builder()
                .nickname(userProfiles.getNickname())
                .city(userProfiles.getCity())
                .state(userProfiles.getState())
                .street(userProfiles.getStreet())
                .introduce(userProfiles.getIntroduce())
                .url(userProfiles.getUrl())
                .url_name(userProfiles.getUrlName())
                .build()
        )
        .build();

    return responseBuilder(signupUserData, HttpStatus.OK);
  }


  /* 이메일 가입여부 */
  public boolean emailConfirm(String email) {
    return usersRepository.findByEmail(email)==null? true : false;
  }

}
