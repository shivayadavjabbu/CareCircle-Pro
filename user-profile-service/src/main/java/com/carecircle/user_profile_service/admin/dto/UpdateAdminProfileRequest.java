package com.carecircle.user_profile_service.admin.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for updating an admin profile.
 */
public class UpdateAdminProfileRequest {

    private String fullName;
    private String phoneNumber;
    private String adminLevel;
    
    private String address;
    private String city;

    public UpdateAdminProfileRequest() {}

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAdminLevel() {
        return adminLevel;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
