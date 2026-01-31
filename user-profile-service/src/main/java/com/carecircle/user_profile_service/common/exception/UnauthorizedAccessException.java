package com.carecircle.user_profile_service.common.exception;

/**
 * Exception thrown when attempting an unauthorized operation.
 */
public class UnauthorizedAccessException extends RuntimeException {
    
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
