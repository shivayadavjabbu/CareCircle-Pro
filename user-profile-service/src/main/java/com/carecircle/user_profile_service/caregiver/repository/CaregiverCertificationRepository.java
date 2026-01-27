package com.carecircle.user_profile_service.caregiver.repository;

import com.carecircle.user_profile_service.caregiver.model.CaregiverCertification;
import com.carecircle.user_profile_service.caregiver.model.CaregiverProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for caregiver certifications.
 */
public interface CaregiverCertificationRepository
        extends JpaRepository<CaregiverCertification, UUID> {

    /**
     * Fetch all certifications for a caregiver.
     */
    List<CaregiverCertification> findAllByCaregiver(CaregiverProfile caregiver);

    /**
     * Fetch a certification owned by a caregiver.
     */
    Optional<CaregiverCertification> findByIdAndCaregiver(
            UUID id,
            CaregiverProfile caregiver
    );

    /**
     * Fetch verified certifications.
     */
    List<CaregiverCertification> findAllByCaregiverAndVerified(
            CaregiverProfile caregiver,
            Boolean verified
    );
}
