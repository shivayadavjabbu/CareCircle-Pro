package com.carecircle.user_profile_service.admin.exception;


/**
 * Thrown when an inactive admin attempts to perform an action.
 */
public class AdminInactiveException extends RuntimeException {

    public AdminInactiveException(String userEmail) {
        super("Admin account is inactive for user: " + userEmail);
    }
}