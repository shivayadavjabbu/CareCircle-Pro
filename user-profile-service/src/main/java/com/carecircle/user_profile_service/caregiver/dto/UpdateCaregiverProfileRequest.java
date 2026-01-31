package com.carecircle.user_profile_service.caregiver.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for updating a caregiver profile.
 * Excludes immutable fields like age and gender.
 */
public class UpdateCaregiverProfileRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Address line 1 is required")
    private String addressLine1;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    private String bio;
    private Integer experienceYears;

    public UpdateCaregiverProfileRequest() {}

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
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

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setAddress(String address) { this.address = address; }
    public void setCity(String city) { this.city = city; }
    public void setBio(String bio) { this.bio = bio; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
}
