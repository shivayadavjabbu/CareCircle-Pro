package com.carecircle.matchingBookingService.booking.api;

import com.carecircle.matchingBookingService.booking.model.Booking;
import com.carecircle.matchingBookingService.booking.repository.BookingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/admin/bookings")
public class AdminBookingController {

    private final BookingRepository bookingRepository;

    public AdminBookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/active")
    public ResponseEntity<Page<Booking>> getActiveBookings(
            @RequestHeader("X-User-Role") String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Admin access required");
        }
        
        Pageable pageable = PageRequest.of(page, limit);
        List<String> activeStatuses = Arrays.asList("REQUESTED", "ACCEPTED");
        return ResponseEntity.ok(bookingRepository.findByStatusIn(activeStatuses, pageable));
    }

    @GetMapping("/completed")
    public ResponseEntity<Page<Booking>> getCompletedBookings(
            @RequestHeader("X-User-Role") String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Admin access required");
        }
        
        Pageable pageable = PageRequest.of(page, limit);
        List<String> completedStatuses = Arrays.asList("COMPLETED");
        return ResponseEntity.ok(bookingRepository.findByStatusIn(completedStatuses, pageable));
    }

    @GetMapping("/cancelled")
    public ResponseEntity<Page<Booking>> getCancelledBookings(
            @RequestHeader("X-User-Role") String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Admin access required");
        }
        
        Pageable pageable = PageRequest.of(page, limit);
        List<String> cancelledStatuses = Arrays.asList("CANCELLED", "REJECTED");
        return ResponseEntity.ok(bookingRepository.findByStatusIn(cancelledStatuses, pageable));
    }
    
    @GetMapping
    public ResponseEntity<Page<Booking>> getAllBookings(
            @RequestHeader("X-User-Role") String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Admin access required");
        }
        
        Pageable pageable = PageRequest.of(page, limit);
        return ResponseEntity.ok(bookingRepository.findAll(pageable));
    }
}
