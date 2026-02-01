package com.carecircle.matchingBookingService.booking.api;

import com.carecircle.matchingBookingService.booking.model.Booking;
import com.carecircle.matchingBookingService.booking.repository.BookingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/caregiver/bookings")
public class CaregiverBookingListController {

    private final BookingRepository bookingRepository;

    public CaregiverBookingListController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getCaregiverBookings(
            @RequestHeader("X-User-Id") UUID caregiverId
    ) {
        return ResponseEntity.ok(bookingRepository.findByCaregiverId(caregiverId));
    }
}
