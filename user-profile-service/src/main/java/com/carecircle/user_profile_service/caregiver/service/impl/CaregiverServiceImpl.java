package com.carecircle.user_profile_service.caregiver.service.impl;

import com.carecircle.user_profile_service.caregiver.exception.CaregiverProfileAlreadyExistsException;
import com.carecircle.user_profile_service.caregiver.exception.CaregiverProfileNotFoundException;
import com.carecircle.user_profile_service.caregiver.model.CaregiverProfile;
import com.carecircle.user_profile_service.caregiver.repository.CaregiverProfileRepository;
import com.carecircle.user_profile_service.caregiver.service.CaregiverService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.carecircle.user_profile_service.common.service.MatchingIntegrationService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of caregiver self-management service.
 *
 * Enforces ownership, lifecycle rules, and prevents unauthorized changes.
 */
@Service
@Transactional
public class CaregiverServiceImpl implements CaregiverService {

    private final CaregiverProfileRepository profileRepository;
    private final MatchingIntegrationService matchingService;

    public CaregiverServiceImpl(
            CaregiverProfileRepository profileRepository,
            MatchingIntegrationService matchingService
    ) {
        this.profileRepository = profileRepository;
        this.matchingService = matchingService;
    }

    // ===== Caregiver Profile =====

    @Override
    public CaregiverProfile createProfile(
    		UUID userId, 
            String userEmail,
            String fullName,
            String phoneNumber,
            Integer age,
            String gender,
            String address,
            String city,
            String bio,
            Integer experienceYears
    ) {
        profileRepository.findByUserEmail(userEmail).ifPresent(existing -> {
        	 throw new CaregiverProfileAlreadyExistsException(userEmail);
        });

        // Validate City
        if (city == null || city.isBlank()) {
             throw new IllegalArgumentException("City is required");
        }
        
        matchingService.getCityByName(city)
                .orElseThrow(() -> new IllegalArgumentException("City not found: " + city));

        CaregiverProfile profile = new CaregiverProfile(
        		userId,
                userEmail,
                fullName,
                phoneNumber,
                age,
                gender,
                address,
                city,
                bio,
                experienceYears
        );

        return profileRepository.save(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public CaregiverProfile getMyProfile(UUID userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new CaregiverProfileNotFoundException(String.valueOf(userId)));
    }

    @Override
    public CaregiverProfile updateMyProfile(
            UUID  userId,
            String fullName,
            String phoneNumber,
            String address,
            String city,
            String bio,
            Integer experienceYears
    ) {
        CaregiverProfile profile = getMyProfile(userId);

        // Validate City if changed
        if (city != null && !city.equals(profile.getCity())) {
            if (city.isBlank()) {
                throw new IllegalArgumentException("City cannot be empty");
            }
            matchingService.getCityByName(city)
                    .orElseThrow(() -> new IllegalArgumentException("City not found: " + city));
            profile.setCity(city);
        }

        // Update only the fields that should change (preserving ID, age, gender, etc.)
        profile.setFullName(fullName);
        profile.setPhoneNumber(phoneNumber);
        profile.setAddress(address);
        profile.setBio(bio);
        profile.setExperienceYears(experienceYears);

        // Save will trigger @PreUpdate to update the updatedAt timestamp
        return profileRepository.save(profile);
    }

    @Override
    public void deleteProfile(UUID userId) {
        CaregiverProfile profile = getMyProfile(userId);
        // Hard delete
        profileRepository.delete(profile);
    }
}

