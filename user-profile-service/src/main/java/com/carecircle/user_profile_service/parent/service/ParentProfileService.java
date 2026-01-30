package com.carecircle.user_profile_service.parent.service;


import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carecircle.user_profile_service.parent.model.ParentProfile;

/**
 * Service responsible for handling parent profile domain logic.
 *
 * This layer coordinates persistence and enforces business rules
 * related to parent profiles.
 */

public interface ParentProfileService {


    /**
     * Creates a new parent profile for the authenticated user.
     *
     * @param userEmail email injected by API Gateway
     * @param fullName parent's full name
     * @param phoneNumber contact phone number
     * @param address residential address
     * @return persisted ParentProfile
     * @throws IllegalStateException if profile already exists
     */
  
    public ParentProfile createProfile(
    		UUID userId, 
            String userEmail,
            String fullName,
            String phoneNumber,
            String address,
            String city
    );

    /**
     * Fetches the parent profile for the authenticated user.
     *
     * @param userEmail email injected by API Gateway
     * @return ParentProfile
     * @throws IllegalStateException if profile does not exist
     */

    public ParentProfile getProfileByUserId(UUID userId);

    /**
     * Updates the parent profile for the authenticated user.
     *
     * @param userId user ID from header
     * @param fullName updated full name
     * @param phoneNumber updated phone number
     * @param address updated address
     * @param city updated city name
     * @return updated ParentProfile
     */
    public ParentProfile updateProfile(
            UUID userId,
            String fullName,
            String phoneNumber,
            String address,
            String city
    );

    /**
     * Deletes the parent profile (hard delete with cascading to children).
     *
     * @param userId user ID from header
     */
    public void deleteProfile(UUID userId);
}
