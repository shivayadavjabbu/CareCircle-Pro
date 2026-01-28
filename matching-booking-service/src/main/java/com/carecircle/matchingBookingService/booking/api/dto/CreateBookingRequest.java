package com.carecircle.matchingBookingService.booking.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class CreateBookingRequest {

    private UUID caregiverId;
    private UUID serviceId;

    private String bookingType; // HOURLY / DAILY

    // hourly
    private LocalTime startTime;
    private LocalTime endTime;

    // daily
    private LocalDate startDate;
    private LocalDate endDate;

    private List<ChildRequest> children;

    public UUID getCaregiverId() {
        return caregiverId;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public String getBookingType() {
        return bookingType;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public List<ChildRequest> getChildren() {
        return children;
    }
}
