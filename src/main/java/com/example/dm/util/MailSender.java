package com.example.dm.util;

import com.example.dm.enums.MailType;
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

//@Slf4j
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

    public void sendEmail(String toEmail, MailType mailType) throws MessagingException, UnsupportedEncodingException {
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

        switch(mailType){
            case EmailVerification:
                message.setSubject(signupSubject, "utf-8");
                message.setContent(signupLink, "text/html; charset=utf-8");
                break;
            case RandomPassword:
                message.setSubject(randomPwdSubject, "utf-8");
                message.setContent(PasswordGenerator.generatePassword(), "text/html; charset=utf-8");
                break;
        }

        transport.send(message);
        transport.close();
    }
}



