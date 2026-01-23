package com.carecircle.user_profile_service.child.dto;


/**
 * Request DTO for updating child details.
 *
 * Ownership and identity are resolved via path variable and user context.
 */
public class UpdateChildRequest {

    private String name;
    private Integer age;
    private String gender;
    private String specialNeeds;

    public UpdateChildRequest() {
        // For JSON deserialization
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getSpecialNeeds() {
        return specialNeeds;
    }
}
