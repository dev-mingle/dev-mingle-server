package com.example.dm.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SecurityConfig 테스트")
class SecurityConfigTest {
  @Autowired
  private SecurityConfig securityConfig;
  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("비밀번호 암호화가 잘 되는지 확인하기")
  void passwordEncoder() {
    String testSentence = "비밀번호 암호화 테스트";
    String encodedSentence = securityConfig.passwordEncoder().encode(testSentence);
    assert securityConfig.passwordEncoder().matches(testSentence, encodedSentence);
  }
}