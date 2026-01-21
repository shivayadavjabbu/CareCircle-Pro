package com.carecircle.auth_service.controller;


import com.carecircle.auth_service.dto.request.LoginRequest;
import com.carecircle.auth_service.dto.request.RegisterRequest;
import com.carecircle.auth_service.dto.response.AuthResponse;
import com.carecircle.auth_service.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/health")
    public String health() {
        return "AUTH OK";
    }
}
