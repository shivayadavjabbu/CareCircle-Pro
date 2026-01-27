package com.carecircle.user_profile_service.parent.dto;

import java.util.UUID;

/*
 * This class helps parent to see who are all people available for the different services  
 * from the CareGiver. The more matching will be done using matching microservice. Which will 
 * take care of who exactly matches. Then we apply more filters and retrive the data from caregiver  tables. 
 */
public class CaregiverSummaryResponse {

    private final UUID id;
    private final String fullName;
    private final String city;
    private final String state;
    private final String gender;
    private final Integer experienceYears;
    private final Double overallRating;
    private final Integer totalReviews;

    public CaregiverSummaryResponse(
            UUID id,
            String fullName,
            String city,
            String state,
            String gender,
            Integer experienceYears,
            Double overallRating,
            Integer totalReviews
    ) {
        this.id = id;
        this.fullName = fullName;
        this.city = city;
        this.state = state;
        this.gender = gender;
        this.experienceYears = experienceYears;
        this.overallRating = overallRating;
        this.totalReviews = totalReviews;
    }

    public UUID getId() { return id; }
    public String getFullName() { return fullName; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getGender() { return gender; }
    public Integer getExperienceYears() { return experienceYears; }
    public Double getOverallRating() { return overallRating; }
    public Integer getTotalReviews() { return totalReviews; }
}
