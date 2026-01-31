package com.carecircle.user_profile_service.parent.dto;

import java.util.UUID;

public class CaregiverDetailResponse {

    private final UUID id;
    private final String fullName;
    private final String bio;
    private final String gender;
    private final Integer age;
    private final String city;
    private final Integer experienceYears;
    private final String address;

    public CaregiverDetailResponse(
            UUID id,
            String fullName,
            String bio,
            String gender,
            Integer age,
            String city,
            Integer experienceYears,
            String address
    ) {
        this.id = id;
        this.fullName = fullName;
        this.bio = bio;
        this.gender = gender;
        this.age = age;
        this.city = city;
        this.experienceYears = experienceYears;
        this.address = address;
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

	public Integer getExperienceYears() {
		return experienceYears;
	}
	
	public String getAddress() {
		return address;
	}
}