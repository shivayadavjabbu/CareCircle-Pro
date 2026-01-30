package com.carecircle.auth_service.loginRegister.service.impl;

import java.net.Authenticator.RequestorType;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.carecircle.auth_service.emailService.service.EmailOtpService;
import com.carecircle.auth_service.loginRegister.dto.request.LoginRequest;
import com.carecircle.auth_service.loginRegister.dto.request.RegisterRequest;
import com.carecircle.auth_service.loginRegister.exception.InvalidCredentialsException;
import com.carecircle.auth_service.loginRegister.exception.UserAlreadyExistsException;
import com.carecircle.auth_service.loginRegister.model.User;
import com.carecircle.auth_service.loginRegister.repository.UserRepository;
import com.carecircle.auth_service.loginRegister.security.JwtUtil;
import com.carecircle.auth_service.loginRegister.service.AuthService;
import com.carecircle.auth_service.loginRegister.dto.request.*;


@Service
public class AuthServiceImpl implements AuthService {

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
        // 1. Check if user already exists
        if (userRepository.findByEmailAndRole(request.getEmail(), request.getRole()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email and role already exists");
        }

        // 2. Hash password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // 3. Send OTP (w/ password stored in EmailOtp)
        emailOtpService.sendOtp(request.getEmail(), request.getRole(), hashedPassword);
    }

    @Override
    public void verifyAccount(VerifyEmailRequest request) {
        // 1. Verify OTP
        var otpResponse = emailOtpService.verifyOtp(request.getEmail(), request.getRole(), request.getOtp());
        
        if (!otpResponse.isSuccess()) {
             throw new InvalidCredentialsException(otpResponse.getMessage());
        }

        // 2. Create actual User
        String storedPassword = otpResponse.getData();
        
        // Double check user doesn't exist (concurrency)
        if (userRepository.findByEmailAndRole(request.getEmail(), request.getRole()).isPresent()) {
            // Should not happen often if verified
             throw new UserAlreadyExistsException("User already verified/exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(storedPassword != null ? storedPassword : ""); // Should not be null if flow followed
        user.setRole(request.getRole());
        user.setEnabled(true);
        
        userRepository.save(user);
    }

    @Override
    public String login(LoginRequest request) {
        User user = userRepository.findByEmailAndRole(request.getEmail(), request.getRole())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid Credentials"));

        if (!user.isEnabled()) {
            throw new InvalidCredentialsException("Account not verified");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid Credentials");
        }

        return jwtUtil.generateToken(user);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
    	if(request.getEmail()==null) {
    		throw new InvalidCredentialsException("User not found");
    	}
    	
    	if(request.getRole()==null) {
    		throw new InvalidCredentialsException("User not found");
    	}
    	
        if (userRepository.findByEmailAndRole(request.getEmail(), request.getRole()) == null) {
             // Return silently or throw specific
             throw new InvalidCredentialsException("User not found");
        }
        // Send OTP with null password (just verification)
        emailOtpService.sendOtp(request.getEmail(), request.getRole(), null);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        // 1. Verify OTP
        var otpResponse = emailOtpService.verifyOtp(request.getEmail(), request.getRole(), request.getOtp());
        if (!otpResponse.isSuccess()) {
             throw new InvalidCredentialsException(otpResponse.getMessage());
        }

        // 2. Setup new password
        User user = userRepository.findByEmailAndRole(request.getEmail(), request.getRole())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
