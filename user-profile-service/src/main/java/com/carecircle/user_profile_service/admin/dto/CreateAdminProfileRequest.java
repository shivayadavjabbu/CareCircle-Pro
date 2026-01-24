package com.carecircle.user_profile_service.admin.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateAdminProfileRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String adminLevel;

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAdminLevel() {
        return adminLevel;
    }
}
