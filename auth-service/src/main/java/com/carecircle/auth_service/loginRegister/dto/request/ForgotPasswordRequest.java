package com.carecircle.auth_service.loginRegister.dto.request;

import com.carecircle.auth_service.loginRegister.model.Role;

public class ForgotPasswordRequest {
    
    private String email;
    private Role role;

    public ForgotPasswordRequest() {}

    public ForgotPasswordRequest(String email, Role role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
