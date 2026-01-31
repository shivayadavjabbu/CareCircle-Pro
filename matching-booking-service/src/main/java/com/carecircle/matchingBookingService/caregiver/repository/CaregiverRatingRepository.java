package com.carecircle.matchingBookingService.caregiver.repository;

import com.carecircle.matchingBookingService.caregiver.model.CaregiverRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CaregiverRatingRepository extends JpaRepository<CaregiverRating, UUID> {
    Optional<CaregiverRating> findByCaregiverId(UUID caregiverId);
}
