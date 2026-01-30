package com.carecircle.auth_service.emailService.service.impl;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carecircle.auth_service.emailService.dto.OtpResponse;
import com.carecircle.auth_service.emailService.model.EmailOtp;
import com.carecircle.auth_service.emailService.repository.EmailOtpRepository;
import com.carecircle.auth_service.emailService.service.EmailOtpService;
import com.carecircle.auth_service.loginRegister.model.Role;
import com.carecircle.auth_service.mail.MailSenderService;

@Service
public class EmailOtpServiceImpl implements EmailOtpService {

    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 3;

    private final EmailOtpRepository otpRepository;
    private final MailSenderService mailSenderService;

    public EmailOtpServiceImpl(
            EmailOtpRepository otpRepository,
            MailSenderService mailSenderService
    ) {
        this.otpRepository = otpRepository;
        this.mailSenderService = mailSenderService;
    }

    @Override
    @Transactional
    public void sendOtp(String email, Role role, String password) {

        // 🔥 clear old OTP
        otpRepository.deleteByEmailAndRole(email, role);

        String otp = generateOtp();

        EmailOtp emailOtp = new EmailOtp();
        emailOtp.setEmail(email);
        emailOtp.setRole(role);
        emailOtp.setOtp(otp);
        if (password != null) {
            emailOtp.setPassword(password);
        }
        emailOtp.setAttempts(0);
        emailOtp.setExpiresAt(
            LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES)
        );

        otpRepository.save(emailOtp);

        mailSenderService.sendOtpMail(email, otp, role.name()); 
    }

    @Override
    @Transactional
    public OtpResponse verifyOtp(String email, Role role, String otp) {

        EmailOtp emailOtp = otpRepository
                .findByEmailAndRole(email, role)
                .orElse(null);

        if (emailOtp == null) {
            return new OtpResponse(false, "OTP not found");
        }

        if (emailOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
            otpRepository.delete(emailOtp);
            return new OtpResponse(false, "OTP expired");
        }

        if (emailOtp.getAttempts() >= MAX_ATTEMPTS) {
            otpRepository.delete(emailOtp);
            return new OtpResponse(false, "Too many attempts");
        }

        if (!emailOtp.getOtp().equals(otp)) {
            emailOtp.setAttempts(emailOtp.getAttempts() + 1);
            otpRepository.save(emailOtp);
            return new OtpResponse(false, "Invalid OTP");
        }

        // ✅ success
        String storedPassword = emailOtp.getPassword();
        otpRepository.delete(emailOtp);
        return new OtpResponse(true, "OTP verified successfully", storedPassword);
    }

    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }
}
