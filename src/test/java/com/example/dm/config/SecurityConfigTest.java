package com.example.dm.config;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SecurityConfig 테스트")
class SecurityConfigTest {
  @Autowired
  private SecurityConfig securityConfig;

  @Test
  @DisplayName("비밀번호 암호화가 잘 되는지 확인하기")
  void passwordEncoder() {
    String testSentence = "비밀번호 암호화 테스트";
    String encodedSentence = securityConfig.passwordEncoder().encode(testSentence);
    assertTrue(securityConfig.passwordEncoder().matches(testSentence, encodedSentence));
  }
}