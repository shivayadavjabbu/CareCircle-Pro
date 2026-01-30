package com.carecircle.user_profile_service.parent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a parent profile.
 *
 * Contains only client-provided data.
 * Validation is applied at the API boundary.
 */
public class CreateParentProfileRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Size	(min = 8, max = 15, message = "Phone number must be between 8 and 15 digits")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    public CreateParentProfileRequest() {
        // For JSON deserialization
    }

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
}