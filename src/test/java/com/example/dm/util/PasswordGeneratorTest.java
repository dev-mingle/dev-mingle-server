package com.example.dm.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("랜덤 비밀번호 생성 테스트")
class PasswordGeneratorTest {
  String password1;
  String password2;

  @BeforeEach
  void generatePassword(){
    password1 = PasswordGenerator.generatePassword();
    password2 = PasswordGenerator.generatePassword();
  }

  @Test
  @DisplayName("랜덤 생성된 패스워드는 길이가 일치하는가?")
  void checkPasswordLength() {
    assertEquals(password1.length(), password2.length());
  }

  @Test
  @DisplayName("랜덤 생성된 패스워드는 중복되지 않는가")
  void checkPasswordIsNotDuplicate() {
    assert !password1.equals(password2);
  }
}