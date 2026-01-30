package com.carecircle.auth_service.loginRegister.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.carecircle.auth_service.emailService.service.EmailOtpService;
import com.carecircle.auth_service.loginRegister.dto.request.LoginRequest;
import com.carecircle.auth_service.loginRegister.dto.request.RegisterRequest;
import com.carecircle.auth_service.loginRegister.exception.AccountNotVerifiedException;
import com.carecircle.auth_service.loginRegister.exception.InvalidCredentialsException;
import com.carecircle.auth_service.loginRegister.exception.UserAlreadyExistsException;
import com.carecircle.auth_service.loginRegister.exception.UserNotFoundException;
import com.carecircle.auth_service.loginRegister.model.User;
import com.carecircle.auth_service.loginRegister.repository.UserRepository;
import com.carecircle.auth_service.loginRegister.security.JwtUtil;
import com.carecircle.auth_service.loginRegister.service.AuthService;
import com.carecircle.auth_service.loginRegister.dto.request.*;


@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailOtpService emailOtpService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil,
                           EmailOtpService emailOtpService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailOtpService = emailOtpService;
    }

    @Override
    public void register(RegisterRequest request) {
        logger.info("Registration attempt for email: {} with role: {}", request.getEmail(), request.getRole());
        
        // 1. Check if user already exists
        if (userRepository.findByEmailAndRole(request.getEmail(), request.getRole()).isPresent()) {
            logger.warn("Registration failed - User already exists: {} with role: {}", 
                    request.getEmail(), request.getRole());
            throw new UserAlreadyExistsException("User with this email and role already exists");
        }

        // 2. Hash password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // 3. Send OTP (w/ password stored in EmailOtp)
        emailOtpService.sendOtp(request.getEmail(), request.getRole(), hashedPassword);
        
        logger.info("Registration OTP sent successfully for email: {}", request.getEmail());
    }

    @Override
    public void verifyAccount(VerifyEmailRequest request) {
        logger.info("Account verification attempt for email: {} with role: {}", 
                request.getEmail(), request.getRole());
        
        // 1. Verify OTP - this will throw exceptions if verification fails
        var otpResponse = emailOtpService.verifyOtp(request.getEmail(), request.getRole(), request.getOtp());

        // 2. Create actual User
        String storedPassword = otpResponse.getData();
        
        // Double check user doesn't exist (concurrency)
        if (userRepository.findByEmailAndRole(request.getEmail(), request.getRole()).isPresent()) {
            logger.warn("Account verification failed - User already exists: {} with role: {}", 
                    request.getEmail(), request.getRole());
            throw new UserAlreadyExistsException("User already verified/exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(storedPassword != null ? storedPassword : ""); // Should not be null if flow followed
        user.setRole(request.getRole());
        user.setEnabled(true);
        
        userRepository.save(user);
        
        logger.info("Account verified and created successfully for email: {} with role: {}", 
                request.getEmail(), request.getRole());
    }

    @Override
    public String login(LoginRequest request) {
        logger.info("Login attempt for email: {} with role: {}", request.getEmail(), request.getRole());
        
        User user = userRepository.findByEmailAndRole(request.getEmail(), request.getRole())
                .orElseThrow(() -> {
                    logger.warn("Login failed - User not found: {} with role: {}", 
                            request.getEmail(), request.getRole());
                    return new UserNotFoundException("Invalid credentials");
                });

        if (!user.isEnabled()) {
            logger.warn("Login failed - Account not verified for email: {} with role: {}", 
                    request.getEmail(), request.getRole());
            throw new AccountNotVerifiedException("Account not verified. Please verify your email first.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.warn("Login failed - Invalid password for email: {} with role: {}", 
                    request.getEmail(), request.getRole());
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);
        logger.info("Login successful for email: {} with role: {}", request.getEmail(), request.getRole());
        
        return token;
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        logger.info("Forgot password request for email: {} with role: {}", 
                request.getEmail(), request.getRole());
        
        if (request.getEmail() == null || request.getRole() == null) {
            logger.warn("Forgot password failed - Missing email or role");
            throw new UserNotFoundException("User not found");
        }
        
        // Check if user exists
        if (!userRepository.findByEmailAndRole(request.getEmail(), request.getRole()).isPresent()) {
            logger.warn("Forgot password failed - User not found: {} with role: {}", 
                    request.getEmail(), request.getRole());
            throw new UserNotFoundException("User not found");
        }
        
        // Send OTP with null password (just verification)
        emailOtpService.sendOtp(request.getEmail(), request.getRole(), null);
        
        logger.info("Forgot password OTP sent successfully for email: {}", request.getEmail());
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        logger.info("Reset password attempt for email: {} with role: {}", 
                request.getEmail(), request.getRole());
        
        // 1. Verify OTP - this will throw exceptions if verification fails
        emailOtpService.verifyOtp(request.getEmail(), request.getRole(), request.getOtp());

        // 2. Setup new password
        User user = userRepository.findByEmailAndRole(request.getEmail(), request.getRole())
                .orElseThrow(() -> {
                    logger.warn("Reset password failed - User not found: {} with role: {}", 
                            request.getEmail(), request.getRole());
                    return new UserNotFoundException("User not found");
                });
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        logger.info("Password reset successfully for email: {} with role: {}", 
                request.getEmail(), request.getRole());
    }
}
