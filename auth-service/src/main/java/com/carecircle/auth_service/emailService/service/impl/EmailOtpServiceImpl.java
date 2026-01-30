package com.carecircle.auth_service.emailService.service.impl;

import java.time.LocalDateTime;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carecircle.auth_service.emailService.dto.OtpResponse;
import com.carecircle.auth_service.emailService.model.EmailOtp;
import com.carecircle.auth_service.emailService.repository.EmailOtpRepository;
import com.carecircle.auth_service.emailService.service.EmailOtpService;
import com.carecircle.auth_service.loginRegister.exception.InvalidOtpException;
import com.carecircle.auth_service.loginRegister.exception.OtpExpiredException;
import com.carecircle.auth_service.loginRegister.exception.OtpNotFoundException;
import com.carecircle.auth_service.loginRegister.exception.TooManyOtpAttemptsException;
import com.carecircle.auth_service.loginRegister.model.Role;
import com.carecircle.auth_service.mail.MailSenderService;

@Service
public class EmailOtpServiceImpl implements EmailOtpService {

    private static final Logger logger = LoggerFactory.getLogger(EmailOtpServiceImpl.class);
    
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
        logger.info("Sending OTP to email: {} for role: {}", email, role);

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
        
        logger.info("OTP sent successfully to email: {} for role: {}", email, role);
    }

    @Override
    @Transactional
    public OtpResponse verifyOtp(String email, Role role, String otp) {
        logger.info("Verifying OTP for email: {} and role: {}", email, role);

        EmailOtp emailOtp = otpRepository
                .findByEmailAndRole(email, role)
                .orElse(null);

        if (emailOtp == null) {
            logger.warn("OTP not found for email: {} and role: {}", email, role);
            throw new OtpNotFoundException("OTP not found. Please request a new OTP.");
        }

        if (emailOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
            logger.warn("OTP expired for email: {} and role: {}. Expired at: {}", 
                    email, role, emailOtp.getExpiresAt());
            otpRepository.delete(emailOtp);
            throw new OtpExpiredException("OTP has expired. Please request a new OTP.");
        }

        if (emailOtp.getAttempts() >= MAX_ATTEMPTS) {
            logger.warn("Too many OTP attempts for email: {} and role: {}. Attempts: {}", 
                    email, role, emailOtp.getAttempts());
            otpRepository.delete(emailOtp);
            throw new TooManyOtpAttemptsException("Too many failed attempts. Please request a new OTP.");
        }

        if (!emailOtp.getOtp().equals(otp)) {
            int newAttempts = emailOtp.getAttempts() + 1;
            emailOtp.setAttempts(newAttempts);
            otpRepository.save(emailOtp);
            
            logger.warn("Invalid OTP for email: {} and role: {}. Attempts: {}/{}", 
                    email, role, newAttempts, MAX_ATTEMPTS);
            throw new InvalidOtpException(
                String.format("Invalid OTP. %d attempts remaining.", MAX_ATTEMPTS - newAttempts)
            );
        }

        // ✅ success
        logger.info("OTP verified successfully for email: {} and role: {}", email, role);
        String storedPassword = emailOtp.getPassword();
        otpRepository.delete(emailOtp);
        return new OtpResponse(true, "OTP verified successfully", storedPassword);
    }

    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }
}
