package com.carecircle.matchingBookingService.booking.repository;

import com.carecircle.matchingBookingService.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByCaregiverIdAndStatus(UUID caregiverId, String status);

    List<Booking> findByParentId(UUID parentId);

    List<Booking> findByCaregiverId(UUID caregiverId);
}
