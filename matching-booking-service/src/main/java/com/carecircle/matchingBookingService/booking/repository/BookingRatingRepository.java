package com.carecircle.matchingBookingService.booking.repository;

import com.carecircle.matchingBookingService.booking.model.BookingRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRatingRepository extends JpaRepository<BookingRating, UUID> {
    
    Optional<BookingRating> findByBookingId(UUID bookingId);
    
    List<BookingRating> findByCaregiverId(UUID caregiverId);
}
