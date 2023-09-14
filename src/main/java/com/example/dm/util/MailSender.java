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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

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

    public void sendOtp(String toEmail) throws MessagingException, UnsupportedEncodingException {
        Properties props = System.getProperties();
        props.put("mail.smtp.port", 25);
        props.put("mail.smtp.host" , "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.fallback", "true");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props, new jakarta.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        MimeMessage message = new MimeMessage(session);
        Transport transport = session.getTransport();

        message.setFrom(new InternetAddress(email, username, "utf-8"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject(signupSubject, "utf-8");

//            template 생성시
//            Reader reader = new InputStreamReader(new ClassPathResource("/mailTemplate.html").getInputStream(), "utf-8");
//            String template = FileCopyUtils.copyToString(reader);
//            String mail = MessageFormat.format(template, );

        message.setContent(signupLink, "text/html; charset=utf-8");
        transport.send(message);
        transport.close();
    }
}



