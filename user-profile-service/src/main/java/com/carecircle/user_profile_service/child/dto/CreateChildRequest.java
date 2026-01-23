package com.carecircle.user_profile_service.child.dto;

/**
 * Request DTO for creating a child.
 *
 * Parent ownership is derived from authenticated user context.
 */
public class CreateChildRequest {

    private String name;
    private Integer age;
    private String gender;
    private String specialNeeds;

    public CreateChildRequest() {
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

