package com.carecircle.matchingBookingService.booking.api;

import com.carecircle.matchingBookingService.booking.model.Booking;
import com.carecircle.matchingBookingService.booking.repository.BookingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class ParentBookingStatusController {

    private final BookingRepository bookingRepository;

    public ParentBookingStatusController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/active")
    public Page<Booking> getActiveBookings(
            @RequestHeader("X-User-Id") UUID parentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        List<String> activeStatuses = Arrays.asList("REQUESTED", "ACCEPTED");
        return bookingRepository.findByParentIdAndStatusIn(parentId, activeStatuses, pageable);
    }

    @GetMapping("/completed")
    public Page<Booking> getCompletedBookings(
            @RequestHeader("X-User-Id") UUID parentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        List<String> completedStatuses = Arrays.asList("COMPLETED");
        return bookingRepository.findByParentIdAndStatusIn(parentId, completedStatuses, pageable);
    }

    @GetMapping("/cancelled")
    public Page<Booking> getCancelledBookings(
            @RequestHeader("X-User-Id") UUID parentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        List<String> cancelledStatuses = Arrays.asList("CANCELLED", "REJECTED");
        return bookingRepository.findByParentIdAndStatusIn(parentId, cancelledStatuses, pageable);
    }
}
