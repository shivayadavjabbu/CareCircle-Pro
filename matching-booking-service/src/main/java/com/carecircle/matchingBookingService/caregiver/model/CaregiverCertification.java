package com.carecircle.matchingBookingService.caregiver.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "caregiver_certifications")
public class CaregiverCertification {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "caregiver_id", nullable = false)
    private UUID caregiverId;

    @Column(name = "service_id")
    private UUID serviceId; // Optional link to a specific service

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "issued_by")
    private String issuedBy;

    @Column(name = "valid_till")
    private LocalDate validTill;

    @Column(name = "verification_status", nullable = false)
    private String verificationStatus; // PENDING / VERIFIED / REJECTED

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected CaregiverCertification() {
        // JPA only
    }

    public CaregiverCertification(
            UUID caregiverId,
            UUID serviceId,
            String name,
            String issuedBy,
            LocalDate validTill
    ) {
        this.caregiverId = caregiverId;
        this.serviceId = serviceId;
        this.name = name;
        this.issuedBy = issuedBy;
        this.validTill = validTill;
        this.verificationStatus = "PENDING";
        this.isActive = true;
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

    public UUID getId() {
        return id;
    }

    public UUID getCaregiverId() {
        return caregiverId;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public String getName() {
        return name;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public LocalDate getValidTill() {
        return validTill;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setVerificationStatus(String verificationStatus) {
    	this.verificationStatus = verificationStatus;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public void setValidTill(LocalDate validTill) {
        this.validTill = validTill;
    }
    
    public void setServiceId(UUID serviceId) {
        this.serviceId = serviceId;
    }
}
