package com.carecircle.auth_service.emailService.dto;

public class OtpResponse {
    private boolean success;
    private String message;

    public OtpResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

	public boolean isSuccess() {
		return success;
	}

	

	public String getMessage() {
		return message;
	}

	

    // getters
    
    
}
