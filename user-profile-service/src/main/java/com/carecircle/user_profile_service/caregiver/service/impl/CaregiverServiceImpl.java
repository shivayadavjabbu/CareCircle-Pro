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

    public CaregiverServiceImpl(
            CaregiverProfileRepository profileRepository,
            CaregiverCapabilityRepository capabilityRepository,
            CaregiverCertificationRepository certificationRepository
    ) {
        this.profileRepository = profileRepository;
        this.capabilityRepository = capabilityRepository;
        this.certificationRepository = certificationRepository;
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
                state,
                pincode,
                country,
                bio,
                experienceYears
        );

        return profileRepository.save(profile);
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

        CaregiverCapability capability = new CaregiverCapability(
                caregiver,
				serviceType,
                description,
                minChildAge,
                maxChildAge,
                requiresCertification
        );

        return capabilityRepository.save(capability);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CaregiverCapability> getMyCapabilities(UUID userId) {
        CaregiverProfile caregiver = getMyProfile(userId);
        return capabilityRepository.findAllByCaregiver(caregiver);
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

        CaregiverCertification certification = new CaregiverCertification(
                caregiver,
                certificationName,
                issuedBy,
                validTill
        );

        return certificationRepository.save(certification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CaregiverCertification> getMyCertifications(UUID userId) {
        CaregiverProfile caregiver = getMyProfile(userId);
        return certificationRepository.findAllByCaregiver(caregiver);
    }
}

