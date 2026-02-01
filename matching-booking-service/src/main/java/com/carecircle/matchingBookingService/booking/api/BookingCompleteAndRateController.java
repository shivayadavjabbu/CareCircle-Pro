package com.carecircle.matchingBookingService.booking.api;

import com.carecircle.matchingBookingService.booking.api.dto.RateBookingRequest;
import com.carecircle.matchingBookingService.booking.model.Booking;
import com.carecircle.matchingBookingService.booking.model.BookingRating;
import com.carecircle.matchingBookingService.booking.repository.BookingRatingRepository;
import com.carecircle.matchingBookingService.booking.repository.BookingRepository;
import com.carecircle.matchingBookingService.caregiver.model.CaregiverRating;
import com.carecircle.matchingBookingService.caregiver.repository.CaregiverRatingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingCompleteAndRateController {

    private final BookingRepository bookingRepository;
    private final BookingRatingRepository bookingRatingRepository;
    private final CaregiverRatingRepository caregiverRatingRepository;

    public BookingCompleteAndRateController(
            BookingRepository bookingRepository,
            BookingRatingRepository bookingRatingRepository,
            CaregiverRatingRepository caregiverRatingRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.bookingRatingRepository = bookingRatingRepository;
        this.caregiverRatingRepository = caregiverRatingRepository;
    }

    @PostMapping("/{bookingId}/complete")
    public ResponseEntity<Booking> completeBooking(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role,
            @PathVariable UUID bookingId
    ) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Only parent can mark as completed
        if (!"ROLE_PARENT".equals(role)) {
            throw new RuntimeException("Only parent can complete a booking");
        }

        if (!booking.getParentId().equals(userId)) {
            throw new RuntimeException("Not authorized to complete this booking");
        }

        if (!"ACCEPTED".equals(booking.getStatus())) {
            throw new RuntimeException("Only ACCEPTED bookings can be marked as COMPLETED");
        }

        booking.complete();
        return ResponseEntity.ok(bookingRepository.save(booking));
    }

    @PostMapping("/{bookingId}/rate")
    public ResponseEntity<BookingRating> rateBooking(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role,
            @PathVariable UUID bookingId,
            @RequestBody RateBookingRequest request
    ) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Only parent can rate
        if (!"ROLE_PARENT".equals(role)) {
            throw new RuntimeException("Only parent can rate a booking");
        }

        if (!booking.getParentId().equals(userId)) {
            throw new RuntimeException("Not authorized to rate this booking");
        }

        if (!"COMPLETED".equals(booking.getStatus())) {
            throw new RuntimeException("Only COMPLETED bookings can be rated");
        }

        // Check if already rated
        if (bookingRatingRepository.findByBookingId(bookingId).isPresent()) {
            throw new RuntimeException("Booking has already been rated");
        }

        // Validate rating
        if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        // Create booking rating
        BookingRating bookingRating = new BookingRating(
                bookingId,
                booking.getCaregiverId(),
                userId,
                request.getRating(),
                request.getReview()
        );
        bookingRating = bookingRatingRepository.save(bookingRating);

        // Update caregiver cumulative rating
        CaregiverRating caregiverRating = caregiverRatingRepository
                .findByCaregiverId(booking.getCaregiverId())
                .orElseGet(() -> {
                    CaregiverRating newRating = new CaregiverRating(booking.getCaregiverId());
                    return caregiverRatingRepository.save(newRating);
                });

        caregiverRating.updateWithNewRating(request.getRating().doubleValue());
        caregiverRatingRepository.save(caregiverRating);

        return ResponseEntity.ok(bookingRating);
    }
}
