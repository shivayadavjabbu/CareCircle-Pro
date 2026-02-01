package com.carecircle.matchingBookingService.booking.api;

import com.carecircle.matchingBookingService.booking.model.Booking;
import com.carecircle.matchingBookingService.booking.repository.BookingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/caregiver/bookings")
public class CaregiverBookingController {

    private final BookingRepository bookingRepository;

    public CaregiverBookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }



    @PostMapping("/{bookingId}/reject")
    public ResponseEntity<Booking> rejectBooking(
            @RequestHeader("X-User-Id") UUID caregiverId,
            @PathVariable UUID bookingId
    ) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();

        if (!booking.getCaregiverId().equals(caregiverId)) {
            throw new RuntimeException("Not authorized to reject this booking");
        }

        if (!"REQUESTED".equals(booking.getStatus())) {
            throw new RuntimeException("Booking is not in REQUESTED state");
        }

        booking.reject();
        return ResponseEntity.ok(bookingRepository.save(booking));
    }
    
    @PostMapping("/{bookingId}/accept")
    public ResponseEntity<Booking> acceptBooking(
            @RequestHeader("X-User-Id") UUID caregiverId,
            @PathVariable UUID bookingId
    ) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();

        if (!booking.getCaregiverId().equals(caregiverId)) {
            throw new RuntimeException("Not authorized to accept this booking");
        }

        if (!"REQUESTED".equals(booking.getStatus())) {
            throw new RuntimeException("Booking is not in REQUESTED state");
        }

        // Hourly overlap check BEFORE accepting
        if ("HOURLY".equals(booking.getBookingType())) {
            boolean conflict =
                    bookingRepository
                            .existsByCaregiverIdAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
                                    caregiverId,
                                    "ACCEPTED",
                                    booking.getEndTime(),
                                    booking.getStartTime()
                             );

            if (conflict) {
                throw new RuntimeException("Booking time conflicts with existing booking");
            }
        }
        
        // Daily overlap check BEFORE accepting
        if ("DAILY".equals(booking.getBookingType())) {
            boolean conflict =
                    bookingRepository
                            .existsByCaregiverIdAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                                    caregiverId,
                                    "ACCEPTED",
                                    booking.getEndDate(),
                                    booking.getStartDate()
                             );

            if (conflict) {
                throw new RuntimeException("Booking date range conflicts with existing booking");
            }
        }
        
        // Only accept after validation passes
        booking.accept();
        return ResponseEntity.ok(bookingRepository.save(booking));

    }
    
    
}
