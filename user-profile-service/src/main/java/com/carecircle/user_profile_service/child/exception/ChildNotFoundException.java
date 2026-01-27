package com.carecircle.user_profile_service.child.exception;


/**
 * Thrown when a child is not found or does not belong to the parent.
 */
public class ChildNotFoundException extends RuntimeException {

    public ChildNotFoundException(String childId) {
        super("Child not found for id: " + childId);
    }
}