package com.carecircle.auth_service.loginRegister.service.impl;

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

        if (userRepository.findByEmailAndRole(request.getEmail(), request.getRole()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email and role already exists");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        
        userRepository.save(user);
    }

    @Override
    public String login(LoginRequest request) {

        User user = userRepository.findByEmailAndRole(request.getEmail(), request.getRole())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid Credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid Credentials"); 
        }

        return jwtUtil.generateToken(user);
    }
}

