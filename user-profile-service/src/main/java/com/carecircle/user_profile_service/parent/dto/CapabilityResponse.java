package com.carecircle.user_profile_service.parent.dto;

public class CapabilityResponse {

    private final String serviceType;
    private final String description;
    private final Integer minChildAge;
    private final Integer maxChildAge;
    private final Double averageRating;
    private final Integer totalReviews;

    public CapabilityResponse(
            String serviceType,
            String description,
            Integer minChildAge,
            Integer maxChildAge,
            Double averageRating,
            Integer totalReviews
    ) {
        this.serviceType = serviceType;
        this.description = description;
        this.minChildAge = minChildAge;
        this.maxChildAge = maxChildAge;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
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

	public Double getAverageRating() {
		return averageRating;
	}

	public Integer getTotalReviews() {
		return totalReviews;
	}

}

