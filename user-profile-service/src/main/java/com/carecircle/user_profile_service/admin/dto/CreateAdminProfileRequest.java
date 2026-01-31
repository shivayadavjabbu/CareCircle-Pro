package com.carecircle.user_profile_service.admin.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public class CreateAdminProfileRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String adminLevel;
    
    private UUID userId; 

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    public CreateAdminProfileRequest() {}

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAdminLevel() {
        return adminLevel;
    }
    
    public UUID getUserId() {
    	return userId; 
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }
}
