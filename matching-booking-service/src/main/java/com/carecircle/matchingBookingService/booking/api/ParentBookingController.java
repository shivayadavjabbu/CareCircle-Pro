package com.carecircle.matchingBookingService.booking.api;

import com.carecircle.matchingBookingService.booking.model.Booking;
import com.carecircle.matchingBookingService.booking.repository.BookingRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class ParentBookingController {

    private final BookingRepository bookingRepository;

    public ParentBookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/my")
    public List<Booking> getMyBookings(
            @RequestHeader("X-User-Id") UUID parentId
    ) {
        return bookingRepository.findByParentId(parentId);
    }
}
