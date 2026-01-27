package com.carecircle.user_profile_service.parent.service;

import com.carecircle.user_profile_service.parent.dto.CaregiverDetailResponse;
import com.carecircle.user_profile_service.parent.dto.CaregiverSummaryResponse;

import java.util.List;
import java.util.UUID;

/**
 * Read-only service for parents to discover verified caregivers.
 */
public interface ParentCaregiverDiscoveryService {

    /**
     * Fetch all verified and active caregivers visible to parents.
     */
    List<CaregiverSummaryResponse> getAllVerifiedCaregivers();

    /**
     * Fetch detailed caregiver profile visible to parents.
     */
    CaregiverDetailResponse getCaregiverById(UUID caregiverId);
}
