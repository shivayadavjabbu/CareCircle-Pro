package com.carecircle.user_profile_service.parent.service.impl;

import com.carecircle.user_profile_service.caregiver.model.CaregiverCapability;
import com.carecircle.user_profile_service.caregiver.model.CaregiverCertification;
import com.carecircle.user_profile_service.caregiver.model.CaregiverProfile;
import com.carecircle.user_profile_service.caregiver.repository.CaregiverCapabilityRepository;
import com.carecircle.user_profile_service.caregiver.repository.CaregiverCertificationRepository;
import com.carecircle.user_profile_service.caregiver.repository.CaregiverProfileRepository;
import com.carecircle.user_profile_service.parent.dto.*;
import com.carecircle.user_profile_service.parent.service.ParentCaregiverDiscoveryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of parent caregiver discovery logic.
 */
@Service
public class ParentCaregiverDiscoveryServiceImpl
        implements ParentCaregiverDiscoveryService {

    private static final String VERIFIED = "VERIFIED";

    private final CaregiverProfileRepository caregiverProfileRepository;
    private final CaregiverCapabilityRepository caregiverCapabilityRepository;
    private final CaregiverCertificationRepository caregiverCertificationRepository;

    public ParentCaregiverDiscoveryServiceImpl(
            CaregiverProfileRepository caregiverProfileRepository,
            CaregiverCapabilityRepository caregiverCapabilityRepository,
            CaregiverCertificationRepository caregiverCertificationRepository
    ) {
        this.caregiverProfileRepository = caregiverProfileRepository;
        this.caregiverCapabilityRepository = caregiverCapabilityRepository;
        this.caregiverCertificationRepository = caregiverCertificationRepository;
    }

    // =========================
    // List caregivers
    // =========================

    @Override
    public List<CaregiverSummaryResponse> getAllVerifiedCaregivers() {

        List<CaregiverProfile> caregivers =
                caregiverProfileRepository
                        .findAllByVerificationStatusAndIsActive(
                                VERIFIED,
                                true
                        );

        return caregivers.stream()
                .map(this::mapToSummaryResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // Get caregiver details
    // =========================

    @Override
    public CaregiverDetailResponse getCaregiverById(Long caregiverId) {

        CaregiverProfile caregiver = caregiverProfileRepository.findById(caregiverId)
                .filter(c ->
                        VERIFIED.equals(c.getVerificationStatus())
                                && Boolean.TRUE.equals(c.getIsActive())
                )
                .orElseThrow(() ->
                        new RuntimeException("Caregiver not found")
                );

        List<CapabilityResponse> capabilities =
                caregiverCapabilityRepository
                        .findAllByCaregiverAndVerified(caregiver, true)
                        .stream()
                        .map(this::mapToCapabilityResponse)
                        .collect(Collectors.toList());

        List<CertificationResponse> certifications =
                caregiverCertificationRepository
                        .findAllByCaregiverAndVerified(caregiver, true)
                        .stream()
                        .map(this::mapToCertificationResponse)
                        .collect(Collectors.toList());

        return mapToDetailResponse(
                caregiver,
                capabilities,
                certifications
        );
    }

    // =========================
    // Mapping helpers
    // =========================

    private CaregiverSummaryResponse mapToSummaryResponse(
            CaregiverProfile caregiver
    ) {
        return new CaregiverSummaryResponse(
                caregiver.getId(),
                caregiver.getFullName(),
                caregiver.getCity(),
                caregiver.getState(),
                caregiver.getGender(),
                caregiver.getExperienceYears(),
                caregiver.getOverallRating(),
                caregiver.getTotalReviews()
        );
    }

    private CaregiverDetailResponse mapToDetailResponse(
            CaregiverProfile caregiver,
            List<CapabilityResponse> capabilities,
            List<CertificationResponse> certifications
    ) {
        return new CaregiverDetailResponse(
                caregiver.getId(),
                caregiver.getFullName(),
                caregiver.getBio(),
                caregiver.getGender(),
                caregiver.getAge(),
                caregiver.getCity(),
                caregiver.getState(),
                caregiver.getExperienceYears(),
                caregiver.getOverallRating(),
                caregiver.getTotalReviews(),
                capabilities,
                certifications
        );
    }

    private CapabilityResponse mapToCapabilityResponse(
            CaregiverCapability capability
    ) {
        return new CapabilityResponse(
                capability.getServiceType(),
                capability.getDescription(),
                capability.getMinChildAge(),
                capability.getMaxChildAge(),
                capability.getAverageRating(),
                capability.getTotalReviews()
        );
    }

    private CertificationResponse mapToCertificationResponse(
            CaregiverCertification certification
    ) {
        return new CertificationResponse(
                certification.getCertificationName(),
                certification.getIssuedBy()
        );
    }
}
