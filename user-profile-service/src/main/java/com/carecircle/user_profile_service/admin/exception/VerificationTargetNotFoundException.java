package com.carecircle.user_profile_service.admin.exception;

import java.util.UUID;

/**
 * Thrown when a verification target entity is not found.
 */
public class VerificationTargetNotFoundException extends RuntimeException {

    public VerificationTargetNotFoundException(String targetType, UUID targetId) {
        super("Verification target not found. Type: " + targetType + ", Id: " + targetId);
    }
}