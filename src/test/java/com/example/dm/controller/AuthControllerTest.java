package com.example.dm.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.dm.dto.users.LoginDto;
import com.example.dm.dto.users.SignupDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AuthController 테스트")
class AuthControllerTest {
  @Resource
  MockMvc mockMvc;
  @Resource
  ObjectMapper objectMapper;

  @Autowired
  PasswordEncoder passwordEncoder;

  // 테스트를 위한 공용 변수
  String email = "devmingle11@gmail.com";
  String nickname = "nickname";
  String password = "Passord1234#";




  @Test
  @DisplayName("이메일 인증발급 확인")
  void sendOtp() {
    try {
      ResultActions resultActions = mockMvc.perform(
          post("/api/v1/users/otp")
              .param("email", email)
              .contentType(MediaType.APPLICATION_JSON)
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
  @DisplayName("닉네임 중복체크 테스트")
  void checkNickname(){
    try {
      ResultActions resultActions = mockMvc.perform(
          post("/api/v1/users/nickname")
              .param("nickname", nickname)
              .contentType(MediaType.APPLICATION_JSON)
      );
      resultActions
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data").value(nickname))
          .andDo(print());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @Rollback(false)
  @DisplayName("회원가입 테스트")
  void signup() {
    SignupDto dto = getSignupDto();
    try {
      ResultActions resultActions = mockMvc.perform(
          post("/api/v1/users")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto))
      );
      resultActions
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data.createdAt").isNotEmpty())
          .andExpect(jsonPath("$.data.email").value(email))
          .andExpect(jsonPath("$.data.userProfile.nickname").value(nickname))
          .andDo(print());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("로그인 테스트")
  void login() {
    LoginDto dto = new LoginDto(email, password);
    try {
      ResultActions resultActions = mockMvc.perform(
          post("/api/v1/users/login")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto))
      );
      resultActions
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.data").value(email))
          .andDo(print());
    } catch (Exception e) {
      e.printStackTrace();
    }
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