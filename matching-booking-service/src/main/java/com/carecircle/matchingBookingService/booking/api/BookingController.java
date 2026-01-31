package com.carecircle.matchingBookingService.booking.api;

import com.carecircle.matchingBookingService.booking.api.dto.BookingDetailResponse;
import com.carecircle.matchingBookingService.booking.api.dto.CreateBookingRequest;
import com.carecircle.matchingBookingService.booking.model.Booking;
import com.carecircle.matchingBookingService.booking.model.BookingChild;
import com.carecircle.matchingBookingService.booking.repository.BookingChildRepository;
import com.carecircle.matchingBookingService.booking.repository.BookingRepository;
import com.carecircle.matchingBookingService.caregiver.repository.CaregiverServiceRepository;
import com.carecircle.matchingBookingService.service.repository.ServiceRepository;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final BookingChildRepository bookingChildRepository;
    private final CaregiverServiceRepository caregiverServiceRepository;
    private final ServiceRepository serviceRepository;

    public BookingController(
            BookingRepository bookingRepository,
            BookingChildRepository bookingChildRepository,
            CaregiverServiceRepository caregiverServiceRepository,
            ServiceRepository serviceRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.bookingChildRepository = bookingChildRepository;
        this.caregiverServiceRepository = caregiverServiceRepository;
        this.serviceRepository = serviceRepository;
    }

    @PostMapping
    public Booking createBooking(
            @RequestHeader("X-User-Id") UUID parentId,
            @RequestBody CreateBookingRequest request
    ) {

        var caregiverService =
                caregiverServiceRepository
                        .findByCaregiverIdAndActiveTrue(request.getCaregiverId())
                        .stream()
                        .filter(cs -> cs.getServiceId().equals(request.getServiceId()))
                        .findFirst()
                        .orElseThrow();

        var service =
                serviceRepository.findById(request.getServiceId()).orElseThrow();

        double pricePerUnit =
                service.getBasePrice() + caregiverService.getExtraPrice();

        int totalUnits;

        if ("HOURLY".equals(request.getBookingType())) {
            totalUnits =
                    (int) ChronoUnit.HOURS.between(
                            request.getStartTime(),
                            request.getEndTime()
                    );
        } else {
            totalUnits =
                    (int) ChronoUnit.DAYS.between(
                            request.getStartDate(),
                            request.getEndDate()
                    ) + 1;
        }



        Booking booking = bookingRepository.save(new Booking(
                parentId,
                request.getCaregiverId(),
                request.getServiceId(),
                request.getBookingType(),
                pricePerUnit,
                totalUnits,
                pricePerUnit * totalUnits
        ));
        
        // Set time/date based on booking type
        if ("HOURLY".equals(request.getBookingType())) {
            booking.setStartTime(request.getStartTime());
            booking.setEndTime(request.getEndTime());
        } else {
            booking.setStartDate(request.getStartDate());
            booking.setEndDate(request.getEndDate());
        }
        
        bookingRepository.save(booking);

        request.getChildren().forEach(child ->
                bookingChildRepository.save(
                        new BookingChild(
                                booking.getId(),
                                child.getChildId(),
                                child.getChildName(),
                                child.getAge(),
                                child.getSpecialNeeds()
                        )
                )
        );

        return booking;
    }
    
    @GetMapping("/{bookingId}")
    public BookingDetailResponse getBooking(
            @PathVariable UUID bookingId
    ) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        var children = bookingChildRepository.findByBookingId(bookingId)
                .stream()
                .map(child -> new BookingDetailResponse.ChildDetail(
                        child.getChildId(),
                        child.getChildName(),
                        child.getAge(),
                        child.getSpecialNeeds()
                ))
                .collect(Collectors.toList());
        
        return new BookingDetailResponse(
                booking.getId(),
                booking.getParentId(),
                booking.getCaregiverId(),
                booking.getServiceId(),
                booking.getBookingType(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getPricePerUnit(),
                booking.getTotalUnits(),
                booking.getFinalPrice(),
                booking.getStatus(),
                children,
                null, // createdAt
                null  // updatedAt
        );
    }
}
