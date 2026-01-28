package com.carecircle.matchingBookingService.booking.api;

import com.carecircle.matchingBookingService.booking.model.Booking;
import com.carecircle.matchingBookingService.booking.repository.BookingRepository;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingCancelController {

    private final BookingRepository bookingRepository;

    public BookingCancelController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @PostMapping("/{bookingId}/cancel")
    public Booking cancelBooking(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role,
            @PathVariable UUID bookingId
    ) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();

        if ("ROLE_PARENT".equals(role)) {
            if (!booking.getParentId().equals(userId)) {
                throw new RuntimeException("Not authorized to cancel this booking");
            }
        } else if ("ROLE_CARETAKER".equals(role)) {
            if (!booking.getCaregiverId().equals(userId)) {
                throw new RuntimeException("Not authorized to cancel this booking");
            }
        } else {
            throw new RuntimeException("Invalid role");
        }

        if (!"REQUESTED".equals(booking.getStatus()) &&
            !"ACCEPTED".equals(booking.getStatus())) {
            throw new RuntimeException("Booking cannot be cancelled in this state");
        }

        booking.cancel();
        return bookingRepository.save(booking);
    }
}

	