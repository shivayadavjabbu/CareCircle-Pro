package com.carecircle.auth_service.loginRegister.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user account has not been verified/enabled.
 */
public class AccountNotVerifiedException extends AuthServiceException {

    private static final String ERROR_CODE = "ACCOUNT_NOT_VERIFIED";
    private static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;

    public AccountNotVerifiedException(String message) {
        super(message, ERROR_CODE, HTTP_STATUS);
    }

    public AccountNotVerifiedException(String message, Throwable cause) {
        super(message, cause, ERROR_CODE, HTTP_STATUS);
    }
}
