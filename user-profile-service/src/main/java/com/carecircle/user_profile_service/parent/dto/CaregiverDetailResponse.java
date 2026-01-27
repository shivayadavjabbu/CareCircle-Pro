package com.carecircle.user_profile_service.parent.dto;

import java.util.List;
import java.util.UUID;

public class CaregiverDetailResponse {

    private final UUID id;
    private final String fullName;
    private final String bio;
    private final String gender;
    private final Integer age;
    private final String city;
    private final String state;
    private final Integer experienceYears;
    private final Double overallRating;
    private final Integer totalReviews;

    private final List<CapabilityResponse> capabilities;
    private final List<CertificationResponse> certifications;

    public CaregiverDetailResponse(
            UUID id,
            String fullName,
            String bio,
            String gender,
            Integer age,
            String city,
            String state,
            Integer experienceYears,
            Double overallRating,
            Integer totalReviews,
            List<CapabilityResponse> capabilities,
            List<CertificationResponse> certifications
    ) {
        this.id = id;
        this.fullName = fullName;
        this.bio = bio;
        this.gender = gender;
        this.age = age;
        this.city = city;
        this.state = state;
        this.experienceYears = experienceYears;
        this.overallRating = overallRating;
        this.totalReviews = totalReviews;
        this.capabilities = capabilities;
        this.certifications = certifications;
    }

	public UUID getId() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public String getBio() {
		return bio;
	}

	public String getGender() {
		return gender;
	}

	public Integer getAge() {
		return age;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public Integer getExperienceYears() {
		return experienceYears;
	}

	public Double getOverallRating() {
		return overallRating;
	}

	public Integer getTotalReviews() {
		return totalReviews;
	}

	public List<CapabilityResponse> getCapabilities() {
		return capabilities;
	}

	public List<CertificationResponse> getCertifications() {
		return certifications;
	}

    
}