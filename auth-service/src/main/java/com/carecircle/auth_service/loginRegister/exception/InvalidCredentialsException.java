package com.carecircle.auth_service.loginRegister.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when authentication credentials are invalid.
 */
public class InvalidCredentialsException extends AuthServiceException {

    private static final String ERROR_CODE = "INVALID_CREDENTIALS";
    private static final HttpStatus HTTP_STATUS = HttpStatus.UNAUTHORIZED;

    public InvalidCredentialsException(String message) {
        super(message, ERROR_CODE, HTTP_STATUS);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause, ERROR_CODE, HTTP_STATUS);
    }
}

