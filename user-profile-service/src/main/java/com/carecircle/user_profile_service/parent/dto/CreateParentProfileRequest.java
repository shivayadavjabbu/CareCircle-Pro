package com.carecircle.user_profile_service.parent.dto;

/**
 * Request DTO for creating a parent profile.
 *
 * This DTO represents client-provided data only.
 * Authenticated user identity is derived from the gateway.
 */
public class CreateParentProfileRequest {

    private String fullName;
    private String phoneNumber;
    private String address;

    public CreateParentProfileRequest() {
        // Default constructor for JSON deserialization
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
}
