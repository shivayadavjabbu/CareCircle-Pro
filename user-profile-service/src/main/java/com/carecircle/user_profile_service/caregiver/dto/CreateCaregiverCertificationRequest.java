package com.carecircle.user_profile_service.caregiver.dto;


import java.time.LocalDate;

/**
 * Request DTO for adding a caregiver certification.
 */
public class CreateCaregiverCertificationRequest {

    private String certificationName;
    private String issuedBy;
    private LocalDate validTill;

    public CreateCaregiverCertificationRequest() {}

    public String getCertificationName() { return certificationName; }
    public String getIssuedBy() { return issuedBy; }
    public LocalDate getValidTill() { return validTill; }
}

