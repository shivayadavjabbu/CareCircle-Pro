package com.carecircle.user_profile_service.admin.exception;

/**
 * Thrown when a verification target entity is not found.
 */
public class VerificationTargetNotFoundException extends RuntimeException {

    public VerificationTargetNotFoundException(String targetType, Long targetId) {
        super("Verification target not found. Type: " + targetType + ", Id: " + targetId);
    }
}