package com.carecircle.auth_service.loginRegister.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an OTP doesn't match the expected value.
 */
public class InvalidOtpException extends AuthServiceException {

    private static final String ERROR_CODE = "INVALID_OTP";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    public InvalidOtpException(String message) {
        super(message, ERROR_CODE, HTTP_STATUS);
    }

    public InvalidOtpException(String message, Throwable cause) {
        super(message, cause, ERROR_CODE, HTTP_STATUS);
    }
}
