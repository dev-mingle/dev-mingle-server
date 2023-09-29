package com.example.dm.util;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Slf4j
@Component
@RequestScope
public class MailSender {
    @Value("${mail.username}")
    private String username;
    @Value("${mail.email}")
    private String email;
    @Value("${mail.password}")
    private String password;
    @Value("${mail.signupSubject}")
    private String signupSubject;
    @Value("${mail.signupLink}")
    private String signupLink;
    @Value("${mail.randomPwdSubject}")
    private String randomPwdSubject;

    /* 이메일 인증 발송 */
    public void sendEmailVerification(String toEmail) throws MessagingException, UnsupportedEncodingException {
        sendMail(toEmail, signupSubject, signupLink);
        log.info("이메일 인증 발송 대상: "+toEmail+" 인증링크: "+signupLink);
    }

    /* 랜덤 비밀번호 발송 */
    public void sendRandomPwd(String toEmail, String randomPassword) throws MessagingException, UnsupportedEncodingException {
        sendMail(toEmail, randomPwdSubject, randomPassword);
        log.info("랜덤 비밀번호 발송 대상: "+toEmail);
    }


    public void sendMail(String toEmail, String subject, String contents) throws MessagingException, UnsupportedEncodingException {
        Properties props = System.getProperties();
        props.put("mail.smtp.port", 25);
        props.put("mail.smtp.host" , "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.fallback", "true");
        props.put("mail.smtp.auth", "true");
        String id = email.substring(0, email.indexOf("@"));

        Session session = Session.getDefaultInstance(props, new jakarta.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(id, password);
            }
        });

        MimeMessage message = new MimeMessage(session);
        Transport transport = session.getTransport();

        message.setFrom(new InternetAddress(email, username, "utf-8"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject(subject, "utf-8");
        message.setContent(contents, "text/html; charset=utf-8");

        transport.send(message);
        transport.close();
    }
}



