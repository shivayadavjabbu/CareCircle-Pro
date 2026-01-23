package com.carecircle.user_profile_service.admin.exception;

/**
 * Thrown when an admin profile is not found for the authenticated user.
 */
public class AdminProfileNotFoundException extends RuntimeException {

    public AdminProfileNotFoundException(String userEmail) {
        super("Admin profile not found for user: " + userEmail);
    }
}
