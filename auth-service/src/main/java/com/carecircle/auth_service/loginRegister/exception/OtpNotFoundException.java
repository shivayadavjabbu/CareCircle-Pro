package com.carecircle.auth_service.loginRegister.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an OTP record is not found.
 */
public class OtpNotFoundException extends AuthServiceException {

    private static final String ERROR_CODE = "OTP_NOT_FOUND";
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    public OtpNotFoundException(String message) {
        super(message, ERROR_CODE, HTTP_STATUS);
    }

    public OtpNotFoundException(String message, Throwable cause) {
        super(message, cause, ERROR_CODE, HTTP_STATUS);
    }
}
