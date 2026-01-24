package com.carecircle.user_profile_service.parent.exception;

/*
 * Thrown when parent role is not present.
 */
public class ParentRoleRequiredException extends RuntimeException {

	public ParentRoleRequiredException(String userEmail) {
        super("Parent profile not found for user: " + userEmail);
    }
}
