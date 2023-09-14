package com.example.dm.controller;

import com.example.dm.dto.ApiResponse;
import com.example.dm.dto.form.SignupForm;
import com.example.dm.dto.form.SignupUserData;
import com.example.dm.dto.form.SignupUserProfilesData;
import com.example.dm.entity.LoginUser;
import com.example.dm.entity.UserProfiles;
import com.example.dm.entity.Users;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.AuthException;
import com.example.dm.exception.BadRequestException;
import com.example.dm.repository.UserProfilesRepository;
import com.example.dm.repository.UsersRepository;
import com.example.dm.security.jwt.TokenProvider;
import com.example.dm.service.AuthService;
import com.example.dm.util.MailSender;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
  private final AuthService authService;
  private final UsersRepository usersRepository;
  private final UserProfilesRepository userProfilesRepository;
  private final MailSender mailSender;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private TokenProvider tokenProvider;


  /* 이메일 인증발급 */
  @PostMapping("/otp")
  public ResponseEntity<ApiResponse> sendOtp(@RequestBody String email) {
    if(!emailConfirm(email)){
      throw new AuthException(ApiResultStatus.ALREADY_SIGNED_UP.getMessage());
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
  public ResponseEntity<ApiResponse> signup(HttpServletResponse response, @Valid @RequestBody SignupForm signupForm) {
    if(!emailConfirm(signupForm.getEmail())){
      throw new AuthException(ApiResultStatus.ALREADY_SIGNED_UP.getMessage());
    }

    Users user = Users.create(signupForm.getEmail(),
                          passwordEncoder.encode(signupForm.getPassword()),
                          signupForm.getProvider()==null? "EMAIL":signupForm.getProvider(),
                          signupForm.getProviderId()==null? "":signupForm.getProviderId());
    usersRepository.save(user);

    UserProfiles userProfiles = UserProfiles.create(user, signupForm);
    userProfilesRepository.save(userProfiles);

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
        .email(user.getEmail())
        .createdAt(user.getCreatedAt())
        .userProfile(signupUserProfilesData)
        .build();

    setAuthentication(response, user.getEmail());
    return responseBuilder(signupUserData, HttpStatus.OK);
  }

  /* 로그인 */
  @GetMapping("/login")
  public ResponseEntity<ApiResponse> login(HttpServletResponse response, @RequestParam("email") String email, @RequestParam("password") String password) {
    Users user = usersRepository.findByEmail(email);
    if(user==null){
      throw new AuthException(ApiResultStatus.USER_NOT_FOUND.getMessage());
    }
    if(!passwordEncoder.matches(password, user.getPassword())){
      throw new AuthException(ApiResultStatus.WRONG_PASSWORD.getMessage());
    }
    setAuthentication(response, email);
    return responseBuilder(true, HttpStatus.OK);
  }


  /* 이메일 가입여부 */
  public boolean emailConfirm(String email) {
    return usersRepository.findByEmail(email)==null? true : false;
  }

  /* token context 설정 */
  public void setAuthentication(HttpServletResponse response, String email) {
    String accessToken="", refreshToken="";

    UserDetails loginUser = authService.loadUserByUsername(email);
    if(loginUser!=null){
      accessToken = tokenProvider.generateAccessToken(loginUser);
      refreshToken = tokenProvider.generateRefreshToken(loginUser);
    }

//    Authentication authentication = tokenProvider.getAuthentication(accessToken);
//    SecurityContextHolder.getContext().setAuthentication(authentication);

    tokenProvider.setAuthenticationTokens(response, accessToken, refreshToken);
  }

}
