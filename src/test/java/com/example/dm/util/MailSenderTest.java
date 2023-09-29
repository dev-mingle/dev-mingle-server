package com.example.dm.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.mail.MessagingException;
import jakarta.mail.SendFailedException;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("메일 전송 테스트")
class MailSenderTest {
  @Autowired
  MailSender mailSender;

  // fromEmail 설정
  // application-auth -> mail.email

  /* 실제 메일 전송됨으로 주의 */
  @Test
  @DisplayName("이메일 인증 메일링 테스트")
  void sendEmailVerification() throws MessagingException, UnsupportedEncodingException {
    mailSender.sendEmailVerification("devmingle11@gamil.com");
  }

  /* 실제 메일 전송됨으로 주의 */
  @Test
  @DisplayName("랜덤 비밀번호 발송 테스트")
  void sendRandomPwd() throws MessagingException, UnsupportedEncodingException {
    mailSender.sendRandomPwd("devmingle11@gmail.com","random_password");
  }

  @Test
  @DisplayName("이메일 형식이 올바르지 않은 경우")
  void exceptionTest() {
    assertThrows(SendFailedException.class, () -> {
      mailSender.sendMail("devmingle11gmail.com", "subject", "contents");});
  }
}