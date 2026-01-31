package com.carecircle.user_profile_service.admin.dto;

import java.util.UUID;

/**
 * Caregiver summary for admin listing.
 */
public class CaregiverSummaryResponse {
    
    private final UUID id;
    private final String fullName;
    private final String userEmail;
    private final String city;
    private final String verificationStatus;
    private final Boolean isActive;
    private final Integer experienceYears;

    public CaregiverSummaryResponse(
            UUID id,
            String fullName,
            String userEmail,
            String city,
            String verificationStatus,
            Boolean isActive,
            Integer experienceYears
    ) {
        this.id = id;
        this.fullName = fullName;
        this.userEmail = userEmail;
        this.city = city;
        this.verificationStatus = verificationStatus;
        this.isActive = isActive;
        this.experienceYears = experienceYears;
    }

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getCity() {
        return city;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }
}
