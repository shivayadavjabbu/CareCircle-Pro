package com.carecircle.user_profile_service.admin.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProfileVerificationAuditResponse {
    private UUID id;
    private UUID adminId;
    private String adminName;
    private String targetType; // CAREGIVER_PROFILE
    private UUID targetId;
    private String targetName; // Caregiver Full Name
    private String action;
    private String previousStatus;
    private String newStatus;
    private String reason;
    private LocalDateTime createdAt;

    public ProfileVerificationAuditResponse() {}

    public ProfileVerificationAuditResponse(
            UUID id, UUID adminId, String adminName, String targetType,
            UUID targetId, String targetName, String action,
            String previousStatus, String newStatus, String reason,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.adminId = adminId;
        this.adminName = adminName;
        this.targetType = targetType;
        this.targetId = targetId;
        this.targetName = targetName;
        this.action = action;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getAdminId() { return adminId; }
    public void setAdminId(UUID adminId) { this.adminId = adminId; }
    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public UUID getTargetId() { return targetId; }
    public void setTargetId(UUID targetId) { this.targetId = targetId; }
    public String getTargetName() { return targetName; }
    public void setTargetName(String targetName) { this.targetName = targetName; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getPreviousStatus() { return previousStatus; }
    public void setPreviousStatus(String previousStatus) { this.previousStatus = previousStatus; }
    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
