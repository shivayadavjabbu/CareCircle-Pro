package com.carecircle.user_profile_service.admin.dto;	

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO representing admin profile details.
 */
public class AdminProfileResponse {

    private final UUID id;
    private final String fullName;
    private final String userEmail;
    private final String adminLevel;
    private final Boolean isActive;
    private final LocalDateTime createdAt;

    public AdminProfileResponse(
            UUID id,
            String fullName,
            String userEmail,
            String adminLevel,
            Boolean isActive,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.fullName = fullName;
        this.userEmail = userEmail;
        this.adminLevel = adminLevel;
        this.isActive = isActive;
        this.createdAt = createdAt;
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

    public String getAdminLevel() {
        return adminLevel;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
