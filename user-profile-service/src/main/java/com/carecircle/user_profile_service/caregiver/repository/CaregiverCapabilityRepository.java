package com.carecircle.user_profile_service.caregiver.repository;

import com.carecircle.user_profile_service.caregiver.model.CaregiverCapability;
import com.carecircle.user_profile_service.caregiver.model.CaregiverProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for caregiver service capabilities.
 */
public interface CaregiverCapabilityRepository
        extends JpaRepository<CaregiverCapability, Long> {

    /**
     * Fetch all capabilities for a caregiver.
     */
    List<CaregiverCapability> findAllByCaregiver(CaregiverProfile caregiver);

    /**
     * Fetch a specific capability owned by a caregiver.
     */
    Optional<CaregiverCapability> findByIdAndCaregiver(
            Long id,
            CaregiverProfile caregiver
    );

    /**
     * Fetch verified capabilities (visible to parents).
     */
    List<CaregiverCapability> findAllByCaregiverAndVerified(
            CaregiverProfile caregiver,
            Boolean verified
    );
}
