package com.carecircle.user_profile_service.caregiver.repository;


import com.carecircle.user_profile_service.caregiver.model.CaregiverProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for CaregiverProfile persistence.
 */
public interface CaregiverProfileRepository extends JpaRepository<CaregiverProfile, Long> {

    /**
     * Fetch caregiver profile by authenticated user email.
     */
    Optional<CaregiverProfile> findByUserEmail(String userEmail);

    /**
     * Fetch all caregivers visible to parents.
     * Used later by search / matching APIs.
     */
    List<CaregiverProfile> findAllByVerificationStatusAndIsActive(
            String verificationStatus,
            Boolean isActive
    );
}

