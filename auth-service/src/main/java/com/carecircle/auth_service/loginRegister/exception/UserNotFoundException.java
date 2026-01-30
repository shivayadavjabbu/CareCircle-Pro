package com.carecircle.auth_service.loginRegister.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user is not found in the system.
 */
public class UserNotFoundException extends AuthServiceException {

    private static final String ERROR_CODE = "USER_NOT_FOUND";
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    public UserNotFoundException(String message) {
        super(message, ERROR_CODE, HTTP_STATUS);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause, ERROR_CODE, HTTP_STATUS);
    }
}
