package com.carecircle.user_profile_service.caregiver.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO representing caregiver profile details.
 */
public class CaregiverProfileResponse {

    private final UUID id;
    private final String fullName;
    private final String userEmail;
    private final String phoneNumber;
    private final Integer age;
    private final String gender;

    private final String address;
    private final String city;

    private final String bio;
    private final Integer experienceYears;

    private final String verificationStatus;
    private final Boolean isActive;

    public CaregiverProfileResponse(
            UUID id,
            String fullName,
            String userEmail,
            String phoneNumber,
            Integer age,
            String gender,
            String address,
            String city,
            String bio,
            Integer experienceYears,
            String verificationStatus,
            Boolean isActive
    ) {
        this.id = id;
        this.fullName = fullName;
        this.userEmail = userEmail;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.city = city;
        this.bio = bio;
        this.experienceYears = experienceYears;
        this.verificationStatus = verificationStatus;
        this.isActive = isActive;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getBio() {
        return bio;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public Boolean getIsActive() {
        return isActive;
    }
}
