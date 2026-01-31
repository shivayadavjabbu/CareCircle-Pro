package com.carecircle.user_profile_service.caregiver.service;


import com.carecircle.user_profile_service.caregiver.model.CaregiverProfile;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for caregiver self-management operations.
 */
public interface CaregiverService {

    // ===== Caregiver Profile =====

    CaregiverProfile createProfile(
    		UUID userId,
            String userEmail,
            String fullName,
            String phoneNumber,
            Integer age,
            String gender,
            String address,
            String city,
            String bio,
            Integer experienceYears
    );

    CaregiverProfile getMyProfile(UUID userId);

    CaregiverProfile updateMyProfile(
    		UUID userId,
            String fullName,
            String phoneNumber,
            String address,
            String city,
            String bio,
            Integer experienceYears
    );
    
    void deleteProfile(UUID userId);

}
