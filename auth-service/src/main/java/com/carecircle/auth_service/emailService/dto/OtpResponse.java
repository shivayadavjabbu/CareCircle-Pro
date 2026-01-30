package com.carecircle.auth_service.emailService.dto;

public class OtpResponse {
    private boolean success;
    private String message;
    private String data; // Added to carry payload (e.g., password)

    public OtpResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public OtpResponse(boolean success, String message, String data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
    
    public String getData() {
        return data;
    }
}
