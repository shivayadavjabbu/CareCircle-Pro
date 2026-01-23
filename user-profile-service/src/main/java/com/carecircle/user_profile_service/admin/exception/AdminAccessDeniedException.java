package com.carecircle.user_profile_service.admin.exception;

/**
 * Thrown when a non-admin user attempts to access admin APIs.
 */
public class AdminAccessDeniedException extends RuntimeException {

    public AdminAccessDeniedException() {
        super("Access denied: admin privileges required");
    }
}
