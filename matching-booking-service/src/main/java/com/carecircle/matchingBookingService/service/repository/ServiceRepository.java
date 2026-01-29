package com.carecircle.matchingBookingService.service.repository;

import com.carecircle.matchingBookingService.service.model.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<ServiceEntity, UUID> {

    List<ServiceEntity> findByActiveTrue();
    java.util.Optional<ServiceEntity> findByCodeIgnoreCaseAndActiveTrue(String code);
}
