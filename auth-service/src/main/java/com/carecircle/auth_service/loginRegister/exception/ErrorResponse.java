package com.carecircle.auth_service.loginRegister.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
	
    private String message;
    
    private LocalDateTime timestamp = LocalDateTime.now();
    
    public ErrorResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

}
