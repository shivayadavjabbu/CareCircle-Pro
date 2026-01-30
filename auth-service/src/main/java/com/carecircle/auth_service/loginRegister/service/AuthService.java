package com.carecircle.auth_service.loginRegister.service;

import com.carecircle.auth_service.loginRegister.dto.request.LoginRequest;
import com.carecircle.auth_service.loginRegister.dto.request.RegisterRequest;
import com.carecircle.auth_service.loginRegister.dto.request.*;

public interface AuthService {

    void register(RegisterRequest request);

    String login(LoginRequest request);
    
    void verifyAccount(VerifyEmailRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}


