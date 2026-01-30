package com.carecircle.user_profile_service.caregiver.service;


import com.carecircle.user_profile_service.caregiver.model.CaregiverCapability;
import com.carecircle.user_profile_service.caregiver.model.CaregiverCertification;
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
            String addressLine1,
            String addressLine2,
            String city,
            String state,
            String pincode,
            String country,
            String bio,
            Integer experienceYears
    );

    CaregiverProfile getMyProfile(UUID userId);

    CaregiverProfile updateMyProfile(
            UUID user_Id, 
            String fullName,
            String phoneNumber,
            String addressLine1,
            String addressLine2,
            String city,
            String state,
            String pincode,
            String country,
            String bio,
            Integer experienceYears
    );
    
    void deleteProfile(UUID userId);

    // ===== Capabilities =====

    CaregiverCapability addCapability(
            UUID userID,
            String serviceType,
            String description,
            Integer minChildAge,
            Integer maxChildAge,
            Boolean requiresCertification
    );

    List<CaregiverCapability> getMyCapabilities(UUID userID);

    void deleteCapability(UUID userId, UUID capabilityId);

    // ===== Certifications =====

    CaregiverCertification addCertification(
            UUID userId, 
            String certificationName,
            String issuedBy,
            java.time.LocalDate validTill
    );

    List<CaregiverCertification> getMyCertifications(UUID userID);
    
    void deleteCertification(UUID userId, UUID certificationId);


}
