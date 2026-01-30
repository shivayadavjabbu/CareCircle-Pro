package com.carecircle.auth_service.loginRegister.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carecircle.auth_service.loginRegister.dto.request.LoginRequest;
import com.carecircle.auth_service.loginRegister.dto.request.RegisterRequest;
import com.carecircle.auth_service.loginRegister.dto.request.VerifyEmailRequest;
import com.carecircle.auth_service.loginRegister.dto.request.ForgotPasswordRequest;
import com.carecircle.auth_service.loginRegister.dto.request.ResetPasswordRequest;
import com.carecircle.auth_service.loginRegister.dto.response.AuthResponse;
import com.carecircle.auth_service.loginRegister.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("OTP sent to email. Please verify to complete registration.");
    }
    
    @PostMapping("/verify-account")
    public ResponseEntity<?> verifyAccount(@RequestBody VerifyEmailRequest request) {
        authService.verifyAccount(request);
        return ResponseEntity.ok("Account verified successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok("Password reset successfully");
    }

    @GetMapping("/health")
    public String health() {
        return "AUTH OK";
    }
}
