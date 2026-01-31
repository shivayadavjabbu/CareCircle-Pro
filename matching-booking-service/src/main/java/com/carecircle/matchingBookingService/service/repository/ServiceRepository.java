package com.carecircle.matchingBookingService.service.repository;

import com.carecircle.matchingBookingService.service.model.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<ServiceEntity, UUID> {

    List<ServiceEntity> findByActiveTrue();
    Optional<ServiceEntity> findByCodeIgnoreCaseAndActiveTrue(String code);
    Optional<ServiceEntity> findByIdAndActiveTrue(UUID id);
}
