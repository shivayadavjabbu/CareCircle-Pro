package com.carecircle.auth_service.emailService.dto;

import com.carecircle.auth_service.loginRegister.model.Role;

public class VerifyOtpRequest {
    private String email;
    private Role role;
    private String otp;
    
    
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
	
	
	public String getOtp() {
		return otp;
	}
	
	
	public void setOtp(String otp) {
		this.otp = otp;
	}

    // getters & setters
    
    
}
