package com.carecircle.matchingBookingService.caregiver.repository;

import com.carecircle.matchingBookingService.caregiver.model.CaregiverService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CaregiverServiceRepository extends JpaRepository<CaregiverService, UUID> {

    List<CaregiverService> findByCaregiverIdAndActiveTrue(UUID caregiverId);

    List<CaregiverService> findByServiceIdAndActiveTrue(UUID serviceId);
}
