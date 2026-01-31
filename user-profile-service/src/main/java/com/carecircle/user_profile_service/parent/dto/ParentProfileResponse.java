package com.carecircle.user_profile_service.parent.dto;


import java.time.LocalDateTime;

/**
 * Response DTO for returning parent profile information.
 *
 * This DTO defines the API contract and must remain stable
 * even if internal domain models change.
 */
public class ParentProfileResponse {

    private final String fullName;
    private final String phoneNumber;
    private final String address;
    private final String city;
    private final LocalDateTime createdAt;

    public ParentProfileResponse(
            String fullName,
            String phoneNumber,
            String address,
            String city,
            LocalDateTime createdAt
    ) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
