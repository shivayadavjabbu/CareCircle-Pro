package com.carecircle.user_profile_service.child.dto;

import java.time.LocalDateTime;

/**
 * Response DTO representing a child.
 */
public class ChildResponse {

    private final Long id;
    private final String name;
    private final Integer age;
    private final String gender;
    private final String specialNeeds;
    private final LocalDateTime createdAt;

    public ChildResponse(
            Long id,
            String name,
            Integer age,
            String gender,
            String specialNeeds,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.specialNeeds = specialNeeds;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
