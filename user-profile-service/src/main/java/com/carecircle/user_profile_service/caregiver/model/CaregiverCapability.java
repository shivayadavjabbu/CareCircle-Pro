package com.carecircle.user_profile_service.caregiver.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a service capability offered by a caregiver.
 *
 * Each capability defines what kind of service the caregiver can provide,
 * for which age group, and whether it has been verified by an admin.
 *
 * Rating fields are aggregated and read-only, updated by the system.
 */
@Entity
@Table(
        name = "caregiver_capabilities",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_caregiver_service_type",
                        columnNames = {"caregiver_id", "service_type"}
                )
        }
)
public class CaregiverCapability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    // ===== Ownership =====

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caregiver_id", nullable = false, updatable = false)
    private CaregiverProfile caregiver;

    // ===== Service Definition =====

    @Column(name = "service_type", nullable = false)
    private String serviceType; // e.g. FEVER_CARE, HOMEWORK_HELP

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "min_child_age")
    private Integer minChildAge;

    @Column(name = "max_child_age")
    private Integer maxChildAge;

    @Column(name = "requires_certification", nullable = false)
    private Boolean requiresCertification;

    // ===== Verification =====

    @Column(name = "verified", nullable = false)
    private Boolean verified = false;

    @Column(name = "verification_note")
    private String verificationNote;

    // ===== Rating Aggregates (READ-ONLY) =====

    @Column(name = "average_rating", nullable = false)
    private Double averageRating = 0.0;

    @Column(name = "total_reviews", nullable = false)
    private Integer totalReviews = 0;

    // ===== Audit =====

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected CaregiverCapability() {
        // JPA only
    }

    public CaregiverCapability(
            CaregiverProfile caregiver,
            String serviceType,
            String description,
            Integer minChildAge,
            Integer maxChildAge,
            Boolean requiresCertification
    ) {
        this.caregiver = caregiver;
        this.serviceType = serviceType;
        this.description = description;
        this.minChildAge = minChildAge;
        this.maxChildAge = maxChildAge;
        this.requiresCertification = requiresCertification;
        this.verified = false;
        this.averageRating = 0.0;
        this.totalReviews = 0;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ===== Getters only (mutations controlled via service) =====

    public UUID getId() {
        return id;
    }

    public CaregiverProfile getCaregiver() {
        return caregiver;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getDescription() {
        return description;
    }

    public Integer getMinChildAge() {
        return minChildAge;
    }

    public Integer getMaxChildAge() {
        return maxChildAge;
    }

    public Boolean getRequiresCertification() {
        return requiresCertification;
    }

    public Boolean getVerified() {
        return verified;
    }

    public String getVerificationNote() {
        return verificationNote;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
 // ===== Verification state changes (admin-only usage) =====

    public void markVerified() {
        this.verified = true;
    }

    public void markUnverified() {
        this.verified = false;
    }
}
