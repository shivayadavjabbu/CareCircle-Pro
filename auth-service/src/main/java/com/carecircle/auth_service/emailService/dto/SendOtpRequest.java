package com.carecircle.auth_service.emailService.dto;

import com.carecircle.auth_service.loginRegister.model.Role;

public class SendOtpRequest {
    private String email;
    private Role role;
    
    
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

    // getters & setters
    
}
