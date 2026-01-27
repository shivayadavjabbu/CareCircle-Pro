package com.carecircle.user_profile_service.caregiver.dto;


import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO representing a caregiver certification.
 */
public class CaregiverCertificationResponse {

    private final UUID id;
    private final String certificationName;
    private final String issuedBy;
    private final Boolean verified;
    private final LocalDateTime createdAt;

    public CaregiverCertificationResponse(
            UUID id,
            String certificationName,
            String issuedBy,
            Boolean verified,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.certificationName = certificationName;
        this.issuedBy = issuedBy;
        this.verified = verified;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getCertificationName() { return certificationName; }
    public String getIssuedBy() { return issuedBy; }
    public Boolean getVerified() { return verified; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
