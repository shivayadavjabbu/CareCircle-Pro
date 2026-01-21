package com.carecircle.auth_service.service;

import com.carecircle.auth_service.dto.request.LoginRequest;
import com.carecircle.auth_service.dto.request.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    String login(LoginRequest request);
    
}


