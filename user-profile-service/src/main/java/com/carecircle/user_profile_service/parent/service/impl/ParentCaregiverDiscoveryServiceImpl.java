package com.carecircle.user_profile_service.parent.service.impl;

import com.carecircle.user_profile_service.caregiver.model.CaregiverProfile;
import com.carecircle.user_profile_service.caregiver.repository.CaregiverProfileRepository;
import com.carecircle.user_profile_service.parent.dto.*;
import com.carecircle.user_profile_service.parent.service.ParentCaregiverDiscoveryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of parent caregiver discovery logic.
 */
@Service
public class ParentCaregiverDiscoveryServiceImpl
        implements ParentCaregiverDiscoveryService {

    private static final String VERIFIED = "VERIFIED";

    private final CaregiverProfileRepository caregiverProfileRepository;

    public ParentCaregiverDiscoveryServiceImpl(
            CaregiverProfileRepository caregiverProfileRepository
    ) {
        this.caregiverProfileRepository = caregiverProfileRepository;
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
    public CaregiverDetailResponse getCaregiverById(UUID caregiverId) {

        CaregiverProfile caregiver = caregiverProfileRepository.findById(caregiverId)
                .filter(c ->
                        VERIFIED.equals(c.getVerificationStatus())
                                && Boolean.TRUE.equals(c.getIsActive())
                )
                .orElseThrow(() ->
                        new RuntimeException("Caregiver not found")
                );

        return mapToDetailResponse(caregiver);
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
                caregiver.getGender(),
                caregiver.getExperienceYears()
        );
    }

    private CaregiverDetailResponse mapToDetailResponse(
            CaregiverProfile caregiver
    ) {
        return new CaregiverDetailResponse(
                caregiver.getId(),
                caregiver.getFullName(),
                caregiver.getBio(),
                caregiver.getGender(),
                caregiver.getAge(),
                caregiver.getCity(),
                caregiver.getExperienceYears(),
                caregiver.getAddress()
        );
    }
}

