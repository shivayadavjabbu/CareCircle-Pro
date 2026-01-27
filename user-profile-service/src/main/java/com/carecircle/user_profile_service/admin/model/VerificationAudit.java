package com.carecircle.user_profile_service.admin.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents an immutable audit record for verification and moderation actions
 * performed by administrators.
 *
 * This entity is append-only and provides full traceability for
 * who changed what, when, and why.
 */
@Entity
@Table(name = "verification_audits")
public class VerificationAudit {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

    // ===== Actor (Admin) =====

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_id", nullable = false, updatable = false)
    private AdminProfile admin;

    // ===== Target (Polymorphic Reference) =====

    @Column(name = "target_type", nullable = false, updatable = false)
    private String targetType;
    // CAREGIVER_PROFILE / CAREGIVER_CAPABILITY / CAREGIVER_CERTIFICATION

    @Column(name = "target_id", nullable = false, updatable = false)
    private UUID targetId;

    // ===== Action & State Transition =====

    @Column(name = "action", nullable = false, updatable = false)
    private String action;
    // VERIFY / REJECT / DISABLE / ENABLE

    @Column(name = "previous_status", nullable = false, updatable = false)
    private String previousStatus;

    @Column(name = "new_status", nullable = false, updatable = false)
    private String newStatus;

    // ===== Metadata =====

    @Column(name = "reason", length = 1000)
    private String reason;

    // ===== Audit =====

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected VerificationAudit() {
        // JPA only
    }

    public VerificationAudit(
            AdminProfile admin,
            String targetType,
            UUID targetId,
            String action,
            String previousStatus,
            String newStatus,
            String reason
    ) {
        this.admin = admin;
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

    // ===== Getters only (immutable entity) =====

    public UUID getId() {
        return id;
    }

    public AdminProfile getAdmin() {
        return admin;
    }

    public String getTargetType() {
        return targetType;
    }

    public UUID getTargetId() {
        return targetId;
    }

    public String getAction() {
        return action;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

