package com.carecircle.auth_service.emailService.service;

import com.carecircle.auth_service.emailService.dto.OtpResponse;
import com.carecircle.auth_service.loginRegister.model.Role;

public interface EmailOtpService {

    void sendOtp(String email, Role role);

    OtpResponse verifyOtp(String email, Role role, String otp);
}
