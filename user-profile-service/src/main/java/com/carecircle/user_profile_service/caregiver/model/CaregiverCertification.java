package com.carecircle.user_profile_service.caregiver.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a certification or formal training held by a caregiver.
 *
 * Certifications are verified by admins and may be used to justify
 * caregiver service capabilities.
 */
@Entity
@Table(name = "caregiver_certifications")
public class CaregiverCertification {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

    // ===== Ownership =====

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caregiver_id", nullable = false, updatable = false)
    private CaregiverProfile caregiver;

    // ===== Certification Details =====

    @Column(name = "certification_name", nullable = false)
    private String certificationName;

    @Column(name = "issued_by")
    private String issuedBy;

    @Column(name = "valid_till")
    private LocalDate validTill;

    @Column(name = "service_id")
    private UUID serviceId;

    // ===== Verification =====

    @Column(name = "verified", nullable = false)
    private Boolean verified = false;

    @Column(name = "verification_note")
    private String verificationNote;

    // ===== Audit =====

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected CaregiverCertification() {
        // JPA only
    }

    public CaregiverCertification(
            CaregiverProfile caregiver,
            String certificationName,
            String issuedBy,
            LocalDate validTill,
            UUID serviceId
    ) {
        this.caregiver = caregiver;
        this.certificationName = certificationName;
        this.issuedBy = issuedBy;
        this.validTill = validTill;
        this.serviceId = serviceId;
        this.verified = false;
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

    // ===== Getters only =====

    public UUID getId() {
        return id;
    }

    public CaregiverProfile getCaregiver() {
        return caregiver;
    }

    public String getCertificationName() {
        return certificationName;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public LocalDate getValidTill() {
        return validTill;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public Boolean getVerified() {
        return verified;
    }

    public String getVerificationNote() {
        return verificationNote;
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

