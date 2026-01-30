package com.carecircle.auth_service.loginRegister.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception class for all auth-service custom exceptions.
 * Extends RuntimeException for unchecked exception behavior.
 * Provides common functionality for error codes and HTTP status mapping.
 */
public abstract class AuthServiceException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;

    /**
     * Constructor with message, error code, and HTTP status.
     *
     * @param message     The error message
     * @param errorCode   A unique error code for this exception type
     * @param httpStatus  The HTTP status code to return
     */
    protected AuthServiceException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    /**
     * Constructor with message, cause, error code, and HTTP status.
     *
     * @param message     The error message
     * @param cause       The underlying cause
     * @param errorCode   A unique error code for this exception type
     * @param httpStatus  The HTTP status code to return
     */
    protected AuthServiceException(String message, Throwable cause, String errorCode, HttpStatus httpStatus) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
