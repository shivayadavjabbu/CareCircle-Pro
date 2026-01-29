package com.carecircle.user_profile_service.caregiver.service.impl;

import com.carecircle.user_profile_service.caregiver.exception.CaregiverProfileAlreadyExistsException;
import com.carecircle.user_profile_service.caregiver.exception.CaregiverProfileNotFoundException;
import com.carecircle.user_profile_service.caregiver.model.CaregiverCapability;
import com.carecircle.user_profile_service.caregiver.model.CaregiverCertification;
import com.carecircle.user_profile_service.caregiver.model.CaregiverProfile;
import com.carecircle.user_profile_service.caregiver.repository.CaregiverCapabilityRepository;
import com.carecircle.user_profile_service.caregiver.repository.CaregiverCertificationRepository;
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
    private final CaregiverCapabilityRepository capabilityRepository;
    private final CaregiverCertificationRepository certificationRepository;
    private final MatchingIntegrationService matchingService;

    public CaregiverServiceImpl(
            CaregiverProfileRepository profileRepository,
            CaregiverCapabilityRepository capabilityRepository,
            CaregiverCertificationRepository certificationRepository,
            MatchingIntegrationService matchingService
    ) {
        this.profileRepository = profileRepository;
        this.capabilityRepository = capabilityRepository;
        this.certificationRepository = certificationRepository;
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
            String addressLine1,
            String addressLine2,
            String city,
            String state,
            String pincode,
            String country,
            String bio,
            Integer experienceYears
    ) {
        profileRepository.findByUserEmail(userEmail).ifPresent(existing -> {
        	 throw new CaregiverProfileAlreadyExistsException(userEmail);
        });

        // Resolve City ID
        UUID cityId = null;
        if (city != null && !city.isBlank()) {
             cityId = matchingService.getCityByName(city)
                    .map(MatchingIntegrationService.CityDto::id)
                    .orElse(null); // Or throw exception if strict
        }

        CaregiverProfile profile = new CaregiverProfile(
        		userId,
                userEmail,
                fullName,
                phoneNumber,
                age,
                gender,
                addressLine1,
                addressLine2,
                city,
                cityId,
                state,
                pincode,
                country,
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
            String addressLine1,
            String addressLine2,
            String city,
            String state,
            String pincode,
            String country,
            String bio,
            Integer experienceYears
    ) {
        CaregiverProfile profile = getMyProfile(userId);

        // Resolve City ID if changed or needed
        UUID cityId = profile.getCityId();
        if (city != null && !city.equals(profile.getCity())) {
             cityId = matchingService.getCityByName(city)
                    .map(MatchingIntegrationService.CityDto::id)
                    .orElse(cityId); 
        }

        // Controlled updates only (no verification, no ratings)
        profile = new CaregiverProfile(
        		profile.getUserId(),
                profile.getUserEmail(),
                fullName,
                phoneNumber,
                profile.getAge(),
                profile.getGender(),
                addressLine1,
                addressLine2,
                city,
                cityId,
                state,
                pincode,
                country,
                bio,
                experienceYears
        );
        
        return profileRepository.save(profile);
    }

    @Override
    public void deleteProfile(UUID userId) {
        CaregiverProfile profile = getMyProfile(userId);
        // Hard delete - this will cascade to capabilities and certifications
        profileRepository.delete(profile);
    }



    // ===== Capabilities =====

    @Override
    public CaregiverCapability addCapability(
            UUID userId,
            String serviceType,
            String description,
            Integer minChildAge,
            Integer maxChildAge,
            Boolean requiresCertification
    ) {
        CaregiverProfile caregiver = getMyProfile(userId);

        // Resolve Service ID
        UUID serviceId = matchingService.getServiceByCode(serviceType)
                .map(MatchingIntegrationService.ServiceDto::id)
                .orElse(null);

        CaregiverCapability capability = new CaregiverCapability(
                caregiver,
				serviceType,
                description,
                minChildAge,
                maxChildAge,
                requiresCertification,
                serviceId
        );

        return capabilityRepository.save(capability);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CaregiverCapability> getMyCapabilities(UUID userId) {
        CaregiverProfile caregiver = getMyProfile(userId);
        return capabilityRepository.findAllByCaregiver(caregiver);
    }

    @Override
    public void deleteCapability(UUID userId, UUID capabilityId) {
        CaregiverProfile caregiver = getMyProfile(userId);
        CaregiverCapability capability = capabilityRepository
                .findById(capabilityId)
                .orElseThrow(() -> new RuntimeException("Capability not found"));
        
        if (!capability.getCaregiver().getId().equals(caregiver.getId())) {
             throw new RuntimeException("Access denied");
        }
        capabilityRepository.delete(capability);
    }

    // ===== Certifications =====

    @Override
    public CaregiverCertification addCertification(
            UUID userId,
            String certificationName,
            String issuedBy,
            LocalDate validTill
    ) {
        CaregiverProfile caregiver = getMyProfile(userId);

        // Resolve Service ID (matching by name for certifications)
        UUID serviceId = matchingService.getServiceByName(certificationName)
                .map(MatchingIntegrationService.ServiceDto::id)
                .orElse(null);

        CaregiverCertification certification = new CaregiverCertification(
                caregiver,
                certificationName,
                issuedBy,
                validTill,
                serviceId
        );

        return certificationRepository.save(certification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CaregiverCertification> getMyCertifications(UUID userId) {
        CaregiverProfile caregiver = getMyProfile(userId);
        return certificationRepository.findAllByCaregiver(caregiver);
    }

    @Override
    public void deleteCertification(UUID userId, UUID certificationId) {
        CaregiverProfile caregiver = getMyProfile(userId);
        CaregiverCertification cert = certificationRepository
                .findById(certificationId)
                .orElseThrow(() -> new RuntimeException("Certification not found"));

        if (!cert.getCaregiver().getId().equals(caregiver.getId())) {
             throw new RuntimeException("Access denied");
        }
        certificationRepository.delete(cert);
    }
}

