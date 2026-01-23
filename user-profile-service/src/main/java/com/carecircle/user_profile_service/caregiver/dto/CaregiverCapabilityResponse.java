package com.carecircle.user_profile_service.caregiver.dto;

import java.time.LocalDateTime;

/**
 * Response DTO representing a caregiver service capability.
 */
public class CaregiverCapabilityResponse {

    private final Long id;
    private final String serviceType;
    private final String description;
    private final Integer minChildAge;
    private final Integer maxChildAge;
    private final Boolean verified;

    private final Double averageRating;
    private final Integer totalReviews;

    private final LocalDateTime createdAt;

    public CaregiverCapabilityResponse(
            Long id,
            String serviceType,
            String description,
            Integer minChildAge,
            Integer maxChildAge,
            Boolean verified,
            Double averageRating,
            Integer totalReviews,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.serviceType = serviceType;
        this.description = description;
        this.minChildAge = minChildAge;
        this.maxChildAge = maxChildAge;
        this.verified = verified;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getServiceType() { return serviceType; }
    public String getDescription() { return description; }
    public Integer getMinChildAge() { return minChildAge; }
    public Integer getMaxChildAge() { return maxChildAge; }
    public Boolean getVerified() { return verified; }
    public Double getAverageRating() { return averageRating; }
    public Integer getTotalReviews() { return totalReviews; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
