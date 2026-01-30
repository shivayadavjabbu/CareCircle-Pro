package com.carecircle.user_profile_service.caregiver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for creating a caregiver profile.
 */
public class CreateCaregiverProfileRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String phoneNumber;

    @NotNull
    private Integer age;

    @NotBlank
    private String gender;

    @NotBlank
    private String addressLine1;

    private String addressLine2;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String pincode;

    @NotBlank
    private String country;

    private String bio;
    private Integer experienceYears;

    public CreateCaregiverProfileRequest() {}

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
}
