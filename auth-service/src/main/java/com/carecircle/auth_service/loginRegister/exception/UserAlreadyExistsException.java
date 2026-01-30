package com.carecircle.auth_service.loginRegister.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when attempting to register a user that already exists.
 */
public class UserAlreadyExistsException extends AuthServiceException {

    private static final String ERROR_CODE = "USER_ALREADY_EXISTS";
    private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

    public UserAlreadyExistsException(String message) {
        super(message, ERROR_CODE, HTTP_STATUS);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause, ERROR_CODE, HTTP_STATUS);
    }
}

