package com.carecircle.auth_service.loginRegister.dto.request;

import com.carecircle.auth_service.loginRegister.model.Role;

public class ResetPasswordRequest {
    
    private String email;
    private String otp;
    private String newPassword;
    private Role role;

    public ResetPasswordRequest() {}

    public ResetPasswordRequest(String email, String otp, String newPassword, Role role) {
        this.email = email;
        this.otp = otp;
        this.newPassword = newPassword;
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
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
