package com.carecircle.auth_service.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

        mailSender.send(message);
    }
}
	