package com.example.dm.controller;

import com.example.dm.dto.ApiResponse;
import com.example.dm.dto.users.LoginDto;
import com.example.dm.dto.users.SignupDto;
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
  public ResponseEntity<ApiResponse> signup(HttpServletResponse response, @Valid @RequestBody SignupDto signupDto) {
    userService.emailConfirm(signupDto.getEmail());
    userService.nicknameConfirm(signupDto.getNickname());

    Users user = Users.create(signupDto.getEmail(),
                              passwordEncoder.encode(signupDto.getPassword()),
                              signupDto.getProvider()==null? "EMAIL": signupDto.getProvider(),
                              signupDto.getProviderId()==null? "": signupDto.getProviderId());
    usersRepository.save(user);

    UserProfiles userProfiles = UserProfiles.create(user, signupDto);
    userProfileRepository.save(userProfiles);

    LoginUser loginUser = authService.loadUserByUsername(signupDto.getEmail());
    authService.setAuthentication(response, loginUser);
    return responseBuilder(userService.setSignupUserData(userProfiles, user), HttpStatus.OK);
  }

  /* 로그인 */
  @PostMapping("/login")
  public ResponseEntity<ApiResponse> login(HttpServletResponse response, @RequestBody LoginDto loginDto) {
    Users user = usersRepository.findByEmailAndIsDeletedIsFalse(loginDto.getEmail()).orElseThrow(
        () -> new AuthException(ApiResultStatus.USER_NOT_FOUND)
    );
    if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
      throw new AuthException(ApiResultStatus.WRONG_PASSWORD);
    }

    LoginUser loginUser = authService.loadUserByUsername(loginDto.getEmail());
    authService.setAuthentication(response, loginUser);
    return responseBuilder(loginDto.getEmail(), HttpStatus.OK);
  }
}
