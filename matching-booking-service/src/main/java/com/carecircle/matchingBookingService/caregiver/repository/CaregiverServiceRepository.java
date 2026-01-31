package com.carecircle.matchingBookingService.caregiver.repository;

import com.carecircle.matchingBookingService.caregiver.model.CaregiverService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CaregiverServiceRepository extends JpaRepository<CaregiverService, UUID> {

    List<CaregiverService> findByCaregiverIdAndActiveTrue(UUID caregiverId);

    List<CaregiverService> findByServiceIdAndActiveTrue(UUID serviceId);

    List<CaregiverService> findByCaregiverId(UUID caregiverId);

    java.util.Optional<CaregiverService> findByCaregiverIdAndServiceId(UUID caregiverId, UUID serviceId);

    // Search Methods
    org.springframework.data.domain.Page<CaregiverService> findByCityAndServiceIdAndActiveTrue(String city, UUID serviceId, org.springframework.data.domain.Pageable pageable);

    org.springframework.data.domain.Page<CaregiverService> findByCityAndActiveTrue(String city, org.springframework.data.domain.Pageable pageable);

    org.springframework.data.domain.Page<CaregiverService> findByServiceIdAndActiveTrue(UUID serviceId, org.springframework.data.domain.Pageable pageable);
    
    org.springframework.data.domain.Page<CaregiverService> findByActiveTrue(org.springframework.data.domain.Pageable pageable);
}
