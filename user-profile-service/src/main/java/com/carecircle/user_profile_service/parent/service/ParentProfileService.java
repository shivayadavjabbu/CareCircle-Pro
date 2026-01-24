package com.carecircle.user_profile_service.parent.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carecircle.user_profile_service.parent.model.ParentProfile;

/**
 * Service responsible for handling parent profile domain logic.
 *
 * This layer coordinates persistence and enforces business rules
 * related to parent profiles.
 */
@Service
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
    @Transactional
    public ParentProfile createProfile(
            String userEmail,
            String fullName,
            String phoneNumber,
            String address
    );

    /**
     * Fetches the parent profile for the authenticated user.
     *
     * @param userEmail email injected by API Gateway
     * @return ParentProfile
     * @throws IllegalStateException if profile does not exist
     */
    @Transactional(readOnly = true)
    public ParentProfile getProfileByUserEmail(String userEmail);
}
