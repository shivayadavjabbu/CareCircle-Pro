package com.carecircle.matchingBookingService.admin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "certification_verification_audits")
public class CertificationVerificationAudit {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "admin_id", nullable = false, updatable = false)
    private UUID adminId;

    @Column(name = "admin_email", nullable = false, updatable = false)
    private String adminEmail;

    @Column(name = "target_type", nullable = false, updatable = false)
    private String targetType; // SERVICE / CERTIFICATION

    @Column(name = "target_id", nullable = false, updatable = false)
    private UUID targetId;

    @Column(name = "action", nullable = false, updatable = false)
    private String action; // VERIFY / REJECT / DISABLE

    @Column(name = "previous_status", nullable = false, updatable = false)
    private String previousStatus;

    @Column(name = "new_status", nullable = false, updatable = false)
    private String newStatus;

    @Column(name = "reason", length = 1000)
    private String reason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected CertificationVerificationAudit() {
        // JPA only
    }

    public CertificationVerificationAudit(
            UUID adminId,
            String adminEmail,
            String targetType,
            UUID targetId,
            String action,
            String previousStatus,
            String newStatus,
            String reason
    ) {
        this.adminId = adminId;
        this.adminEmail = adminEmail;
        this.targetType = targetType;
        this.targetId = targetId;
        this.action = action;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.reason = reason;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public UUID getAdminId() { return adminId; }
    public String getAdminEmail() { return adminEmail; }
    public String getTargetType() { return targetType; }
    public UUID getTargetId() { return targetId; }
    public String getAction() { return action; }
    public String getPreviousStatus() { return previousStatus; }
    public String getNewStatus() { return newStatus; }
    public String getReason() { return reason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
