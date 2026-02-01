package com.carecircle.user_profile_service.caregiver.repository;


import com.carecircle.user_profile_service.caregiver.model.CaregiverProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for CaregiverProfile persistence.
 */
public interface CaregiverProfileRepository extends JpaRepository<CaregiverProfile, UUID> {

    /**
     * Fetch caregiver profile by authenticated user email.
     */
    Optional<CaregiverProfile> findByUserEmail(String userEmail);
    Optional<CaregiverProfile> findByUserId(UUID userId);
    
    List<CaregiverProfile> findByUserIdIn(List<UUID> userIds);

    Page<CaregiverProfile> findByCityIgnoreCase(String city, Pageable pageable);
    Page<CaregiverProfile> findAll(Pageable pageable);

    /**
     * Fetch all caregivers visible to parents.
     * Used later by search / matching APIs.
     */
    List<CaregiverProfile> findAllByVerificationStatusAndIsActive(
            String verificationStatus,
            Boolean isActive
    );
}

