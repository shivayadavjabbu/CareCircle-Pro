package com.carecircle.auth_service.loginRegister.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when maximum OTP verification attempts have been exceeded.
 */
public class TooManyOtpAttemptsException extends AuthServiceException {

    private static final String ERROR_CODE = "TOO_MANY_OTP_ATTEMPTS";
    private static final HttpStatus HTTP_STATUS = HttpStatus.TOO_MANY_REQUESTS;

    public TooManyOtpAttemptsException(String message) {
        super(message, ERROR_CODE, HTTP_STATUS);
    }

    public TooManyOtpAttemptsException(String message, Throwable cause) {
        super(message, cause, ERROR_CODE, HTTP_STATUS);
    }
}
