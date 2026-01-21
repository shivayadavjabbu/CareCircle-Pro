package com.carecircle.auth_service.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.carecircle.auth_service.dto.request.LoginRequest;
import com.carecircle.auth_service.dto.request.RegisterRequest;
import com.carecircle.auth_service.exception.InvalidCredentialsException;
import com.carecircle.auth_service.exception.UserAlreadyExistsException;
import com.carecircle.auth_service.model.User;
import com.carecircle.auth_service.repository.UserRepository;
import com.carecircle.auth_service.security.JwtUtil;
import com.carecircle.auth_service.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered"); 
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);
    }

    @Override
    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid Credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid Credentials"); 
        }

        return jwtUtil.generateToken(user);
    }
}
