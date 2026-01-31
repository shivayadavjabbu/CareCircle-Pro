package com.carecircle.matchingBookingService.caregiver.repository;

import com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CaregiverCertificationRepository extends JpaRepository<CaregiverCertification, UUID> {
    List<CaregiverCertification> findByCaregiverId(UUID caregiverId);

    java.util.Optional<CaregiverCertification> findByCaregiverIdAndServiceId(UUID caregiverId, UUID serviceId);
}
