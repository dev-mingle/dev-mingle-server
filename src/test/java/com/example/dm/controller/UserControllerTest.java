package com.example.dm.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.dm.dto.users.ChangePwdDto;
import com.example.dm.dto.users.MypageDto;
import com.example.dm.dto.users.SignupDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("UserController 테스트")
class UserControllerTest {
  @Resource
  MockMvc mockMvc;
  @Resource
  ObjectMapper objectMapper;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Value("${jwt.access.header}")
  private String ACCESS_HEADER;

  @Value("${jwt.refresh.header}")
  private String REFRESH_HEADER;

  // 테스트를 위한 공용 변수
  String email = "devmingle@gmail.com";
  String nickname = "nickname";
  String changedNickname = "changedNickname";
  String password = "Passord1234#";
  String accessToken, refreshToken;


  @BeforeEach
  @DisplayName("먼저 회원가입 실행 후 토큰 가져오기")
  void setUp() throws Exception {
    SignupDto dto = getSignupDto();
    ResultActions resultActions = mockMvc.perform(
        post("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
    );

    // 토큰 추출
    accessToken = "Bearer " + resultActions.andReturn().getResponse().getHeader(ACCESS_HEADER);
    refreshToken = "Bearer " + resultActions.andReturn().getResponse().getHeader(REFRESH_HEADER);
  }

  @Test
  @DisplayName("회원정보 조회 테스트")
  void getProfiles() {
    try {
      ResultActions resultActions = mockMvc.perform(
          get("/api/v1/users/profile")
              .header(ACCESS_HEADER, accessToken)
              .header(REFRESH_HEADER, refreshToken)
      );
      resultActions
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.email").value(email))
          .andDo(print());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("회원정보 수정 테스트")
  void editProfiles() {
    MypageDto dto = getMypageDto();
    try {
      ResultActions resultActions = mockMvc.perform(
          put("/api/v1/users/profile")
              .header(ACCESS_HEADER, accessToken)
              .header(REFRESH_HEADER, refreshToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto))
      );
      resultActions
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.email").value(email))
          .andExpect(jsonPath("$.data.userProfile.nickname").value(changedNickname))
          .andDo(print());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("비밀번호 초기화 테스트")
  void resetPassword() {
    Map<String, String> map = new HashMap<>();
    map.put("password",password);
    try {
      ResultActions resultActions = mockMvc.perform(
          post("/api/v1/users/password/reset")
              .header(ACCESS_HEADER, accessToken)
              .header(REFRESH_HEADER, refreshToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(map))
      );
      resultActions
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data").value(email))
          .andDo(print());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("비밀번호 변경 테스트")
  void changePassword() {
    try {
      ChangePwdDto dto = new ChangePwdDto(password, "password1234##");
      ResultActions resultActions = mockMvc.perform(
          post("/api/v1/users/password/change")
              .header(ACCESS_HEADER, accessToken)
              .header(REFRESH_HEADER, refreshToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto))
      );
      resultActions
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data").value(true))
          .andDo(print());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("랜덤 비밀번호 전송 테스트")
  void isRandomPassword() {
    try {
      ResultActions resultActions = mockMvc.perform(
          post("/api/v1/users/password/reset/confirm")
              .header(ACCESS_HEADER, accessToken)
              .header(REFRESH_HEADER, refreshToken)
      );
      resultActions
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.email").value(email))
          .andDo(print());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("회원탈퇴 테스트")
  void deleteUser() {
    try {
      ResultActions resultActions = mockMvc.perform(
          delete("/api/v1/users")
              .header(ACCESS_HEADER, accessToken)
              .header(REFRESH_HEADER, refreshToken)
      );
      resultActions
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data").value(email))
          .andDo(print());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private MypageDto getMypageDto() {
    return MypageDto.builder()
        .id(1L)
        .nickname(changedNickname)
        .city("city")
        .state("state")
        .street("street")
        .build();
  }

  private SignupDto getSignupDto() {
    return SignupDto.builder()
        .email(email)
        .password(password)
        .nickname(nickname)
        .city("city")
        .state("state")
        .street("street")
        .build();
  }
}