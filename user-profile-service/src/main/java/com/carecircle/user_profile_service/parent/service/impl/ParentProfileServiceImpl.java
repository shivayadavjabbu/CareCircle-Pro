package com.carecircle.user_profile_service.parent.service.impl;


import com.carecircle.user_profile_service.parent.exception.ParentProfileAlreadyExistsException;
import com.carecircle.user_profile_service.parent.exception.ParentProfileNotFoundException;
import com.carecircle.user_profile_service.parent.model.ParentProfile;
import com.carecircle.user_profile_service.parent.repository.ParentProfileRepository;
import com.carecircle.user_profile_service.parent.service.ParentProfileService;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carecircle.user_profile_service.common.service.MatchingIntegrationService;

/**
 * Service responsible for handling parent profile domain logic.
 *
 * This layer coordinates persistence and enforces business rules
 * related to parent profiles.
 */
@Service
public class ParentProfileServiceImpl implements ParentProfileService{

    private final ParentProfileRepository parentProfileRepository;
    private final MatchingIntegrationService matchingService;

    public ParentProfileServiceImpl(
            ParentProfileRepository parentProfileRepository,
            MatchingIntegrationService matchingService
    ) {
        this.parentProfileRepository = parentProfileRepository;
        this.matchingService = matchingService;
    }

    /**
     * Creates a new parent profile for the authenticated user.
     *
     * @param userEmail email injected by API Gateway
     * @param fullName parent's full name
     * @param phoneNumber contact phone number
     * @param address residential address
     * @return persisted ParentProfile
     * @throws IllegalStateException if profile already exists
     */
    @Transactional
    public ParentProfile createProfile(
    		UUID userId,
            String userEmail,
            String fullName,
            String phoneNumber,
            String address,
            String city
    ) {
        boolean exists = parentProfileRepository.findByUserEmail(userEmail).isPresent();
        if (exists) {
        	 throw new ParentProfileAlreadyExistsException(userEmail);
        }

        // Validate City
        if (city == null || city.isBlank()) {
             throw new IllegalArgumentException("City is required");
        }
        
//        matchingService.getCityByName(city)
//                .orElseThrow(() -> new IllegalArgumentException("City not found: " + city));

        ParentProfile profile =
                new ParentProfile(userId, userEmail, fullName, phoneNumber, address, city);

        return parentProfileRepository.save(profile);
    }

    /**
     * Fetches the parent profile for the authenticated user.
     *
     * @param userEmail email injected by API Gateway
     * @return ParentProfile
     * @throws IllegalStateException if profile does not exist
     */
    @Transactional(readOnly = true)
    public ParentProfile getProfileByUserEmail(String emailId) {
        return parentProfileRepository
                .findByUserEmail(emailId)
                .orElseThrow(() ->
                        new ParentProfileNotFoundException(emailId)
                );
    }


	@Override
	public ParentProfile getProfileByUserId(UUID userId) {
		 return parentProfileRepository
	                .findByUserId(userId)
	                .orElseThrow(() ->
	                        new ParentProfileNotFoundException(String.valueOf(userId))
	                );
	}

	@Override
	public ParentProfile updateProfile(
			UUID userId,
			String fullName,
			String phoneNumber,
			String address,
			String city
	) {
		ParentProfile profile = getProfileByUserId(userId);

		// Validate City if changed
		if (city != null && !city.equals(profile.getCity())) {
			if (city.isBlank()) {
                throw new IllegalArgumentException("City cannot be empty");
            }
            matchingService.getCityByName(city)
                    .orElseThrow(() -> new IllegalArgumentException("City not found: " + city));
			profile.setCity(city);
		}

		// Update only the fields that should change (preserving ID, userId, etc.)
		profile.setFullName(fullName);
		profile.setPhoneNumber(phoneNumber);
		profile.setAddress(address);

		// Save will trigger @PreUpdate to update the updatedAt timestamp
		return parentProfileRepository.save(profile);
	}

	@Override
	public void deleteProfile(UUID userId) {
		ParentProfile profile = getProfileByUserId(userId);
		// Hard delete - will cascade to children
		parentProfileRepository.delete(profile);
	}
}
