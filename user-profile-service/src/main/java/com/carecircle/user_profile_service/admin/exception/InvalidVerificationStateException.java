package com.carecircle.user_profile_service.admin.exception;

/**
 * Thrown when an invalid verification state transition is attempted.
 */
public class InvalidVerificationStateException extends RuntimeException {

    public InvalidVerificationStateException(String message) {
        super(message);
    }
}
