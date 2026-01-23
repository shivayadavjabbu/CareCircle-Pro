package com.carecircle.user_profile_service.caregiver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for adding a caregiver capability.
 */
public class CreateCaregiverCapabilityRequest {

    @NotBlank
    private String serviceType;

    private String description;

    private Integer minChildAge;
    private Integer maxChildAge;

    @NotNull
    private Boolean requiresCertification;

    public CreateCaregiverCapabilityRequest() {}

    public String getServiceType() { return serviceType; }
    public String getDescription() { return description; }
    public Integer getMinChildAge() { return minChildAge; }
    public Integer getMaxChildAge() { return maxChildAge; }
    public Boolean getRequiresCertification() { return requiresCertification; }
}

