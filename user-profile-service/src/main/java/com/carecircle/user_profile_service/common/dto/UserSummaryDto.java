package com.carecircle.user_profile_service.common.dto;

import java.util.UUID;

public class UserSummaryDto {
    private UUID userId;
    private String fullName;
    private String userRole;

    public UserSummaryDto() {}

    public UserSummaryDto(UUID userId, String fullName, String userRole) {
        this.userId = userId;
        this.fullName = fullName;
        this.userRole = userRole;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
