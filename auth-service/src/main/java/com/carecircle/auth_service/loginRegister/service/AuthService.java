package com.carecircle.auth_service.loginRegister.service;

import com.carecircle.auth_service.loginRegister.dto.request.LoginRequest;
import com.carecircle.auth_service.loginRegister.dto.request.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    String login(LoginRequest request);
    
}


