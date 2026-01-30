package com.carecircle.auth_service.loginRegister.dto.request;

import com.carecircle.auth_service.loginRegister.model.Role;

public class VerifyEmailRequest {
    
    private String email;
    private String otp;
    private Role role;

    public VerifyEmailRequest() {}

    public VerifyEmailRequest(String email, String otp, Role role) {
        this.email = email;
        this.otp = otp;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getOtp() {
        return otp;
    }
    public void setOtp(String otp) {
        this.otp = otp;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
