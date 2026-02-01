package com.carecircle.auth_service.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {

    private final JavaMailSender mailSender;

    public MailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpMail(String to, String otp, String role) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("CareCircle OTP Verification");

        message.setText(
            "Hi,\n\n"
          + "Your OTP for CareCircle (" + role + ") is:\n\n"
          + otp + "\n\n"
          + "Valid for 5 minutes.\n\n"
          + "— CareCircle Team"
        );
        System.out.println();
        System.out.println("=== MAIL DEBUG ===");
        
        JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;

        System.out.println("=== MAIL DEBUG ===");
        System.out.println("Host: " + impl.getHost());
        System.out.println("Port: " + impl.getPort());
        System.out.println("Username: " + impl.getUsername());
        impl.getJavaMailProperties().forEach(
            (k, v) -> System.out.println(k + " = " + v)
        );
//        System.out.println("Host: " + message.);
//        System.out.println("Port: " + message.getPort());
//        System.out.println("Username: " + message.getUsername());
        
        mailSender.send(message);
    }
}
	