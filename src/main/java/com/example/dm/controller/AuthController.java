package com.example.dm.controller;

import com.example.dm.dto.ApiResponse;
import com.example.dm.dto.form.LoginForm;
import com.example.dm.dto.form.SignupForm;
import com.example.dm.entity.LoginUser;
import com.example.dm.entity.UserProfiles;
import com.example.dm.entity.Users;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.AuthException;
import com.example.dm.repository.UserProfileRepository;
import com.example.dm.repository.UsersRepository;
import com.example.dm.security.jwt.TokenProvider;
import com.example.dm.service.AuthService;
import com.example.dm.service.UserService;
import com.example.dm.util.MailSender;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path.default}/users")
public class AuthController extends BaseController {
  private final AuthService authService;
  private final UserService userService;
  private final UsersRepository usersRepository;
  private final UserProfileRepository userProfileRepository;
  private final MailSender mailSender;
  private final PasswordEncoder passwordEncoder;

  /* 이메일 인증발급 */
  @PostMapping("/otp")
  public ResponseEntity<ApiResponse> sendOtp(@RequestParam("email") String email) {
    userService.emailConfirm(email);
    try {
      mailSender.sendEmailVerification(email);
    } catch (MessagingException e) {
      throw new AuthException(ApiResultStatus.SEND_MAIL_FAILED);
    } catch (UnsupportedEncodingException e) {
      throw new AuthException(ApiResultStatus.ENCODING_ISSUE);
    }
    return responseBuilder(email, HttpStatus.OK);
  }

  /* 닉네임 체크 */
  @PostMapping("/nickname")
  public ResponseEntity<ApiResponse> checkNickname(@RequestParam("nickname") String nickname) {
    userService.nicknameConfirm(nickname);
    return responseBuilder(nickname, HttpStatus.OK);
  }

  /* 회원가입 */
  @PostMapping
  public ResponseEntity<ApiResponse> signup(HttpServletResponse response, @Valid @RequestBody SignupForm signupForm) {
    userService.emailConfirm(signupForm.getEmail());
    userService.nicknameConfirm(signupForm.getNickname());

    Users user = Users.create(signupForm.getEmail(),
                              passwordEncoder.encode(signupForm.getPassword()),
                              signupForm.getProvider()==null? "EMAIL":signupForm.getProvider(),
                              signupForm.getProviderId()==null? "":signupForm.getProviderId());
    usersRepository.save(user);

    UserProfiles userProfiles = UserProfiles.create(user, signupForm);
    userProfileRepository.save(userProfiles);

    LoginUser loginUser = authService.loadUserByUsername(signupForm.getEmail());
    authService.setAuthentication(response, loginUser);
    return responseBuilder(userService.setSignupUserData(userProfiles, user), HttpStatus.OK);
  }

  /* 로그인 */
  @PostMapping("/login")
  public ResponseEntity<ApiResponse> login(HttpServletResponse response, @RequestBody LoginForm loginForm) {
    Users user = usersRepository.findByEmailAndIsDeletedIsFalse(loginForm.getEmail()).orElseThrow(
        () -> new AuthException(ApiResultStatus.USER_NOT_FOUND)
    );
    if(!passwordEncoder.matches(loginForm.getPassword(), user.getPassword())){
      throw new AuthException(ApiResultStatus.WRONG_PASSWORD);
    }

    LoginUser loginUser = authService.loadUserByUsername(loginForm.getEmail());
    authService.setAuthentication(response, loginUser);
    return responseBuilder(loginForm.getEmail(), HttpStatus.OK);
  }
}
