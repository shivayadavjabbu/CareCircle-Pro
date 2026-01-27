package com.carecircle.user_profile_service.caregiver.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO representing caregiver profile details.
 */
public class CaregiverProfileResponse {

    private final UUID id;
    private final String fullName;
    private final String phoneNumber;
    private final Integer age;
    private final String gender;

    private final String addressLine1;
    private final String addressLine2;
    private final String city;
    private final String state;
    private final String pincode;
    private final String country;

    private final String bio;
    private final Integer experienceYears;

    private final String verificationStatus;
    private final Boolean isActive;

    private final Double overallRating;
    private final Integer totalReviews;

    private final LocalDateTime createdAt;

    public CaregiverProfileResponse(
            UUID id,
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
            Integer experienceYears,
            String verificationStatus,
            Boolean isActive,
            Double overallRating,
            Integer totalReviews,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.gender = gender;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.country = country;
        this.bio = bio;
        this.experienceYears = experienceYears;
        this.verificationStatus = verificationStatus;
        this.isActive = isActive;
        this.overallRating = overallRating;
        this.totalReviews = totalReviews;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getFullName() { return fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public Integer getAge() { return age; }
    public String getGender() { return gender; }
    public String getAddressLine1() { return addressLine1; }
    public String getAddressLine2() { return addressLine2; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getPincode() { return pincode; }
    public String getCountry() { return country; }
    public String getBio() { return bio; }
    public Integer getExperienceYears() { return experienceYears; }
    public String getVerificationStatus() { return verificationStatus; }
    public Boolean getIsActive() { return isActive; }
    public Double getOverallRating() { return overallRating; }
    public Integer getTotalReviews() { return totalReviews; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

