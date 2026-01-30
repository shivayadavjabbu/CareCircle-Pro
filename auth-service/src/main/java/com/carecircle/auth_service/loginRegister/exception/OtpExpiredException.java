package com.carecircle.auth_service.loginRegister.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an OTP has expired.
 */
public class OtpExpiredException extends AuthServiceException {

    private static final String ERROR_CODE = "OTP_EXPIRED";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

    public OtpExpiredException(String message) {
        super(message, ERROR_CODE, HTTP_STATUS);
    }

    public OtpExpiredException(String message, Throwable cause) {
        super(message, cause, ERROR_CODE, HTTP_STATUS);
    }
}
