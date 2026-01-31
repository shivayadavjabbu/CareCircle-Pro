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
    private final String gender;
    private final Integer experienceYears;

    public CaregiverSummaryResponse(
            UUID id,
            String fullName,
            String city,
            String gender,
            Integer experienceYears
    ) {
        this.id = id;
        this.fullName = fullName;
        this.city = city;
        this.gender = gender;
        this.experienceYears = experienceYears;
    }

    public UUID getId() { return id; }
    public String getFullName() { return fullName; }
    public String getCity() { return city; }
    public String getGender() { return gender; }
    public Integer getExperienceYears() { return experienceYears; }
}
