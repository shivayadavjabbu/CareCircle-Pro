package com.carecircle.matchingBookingService.availability.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateAvailabilityRequest {

    private LocalDate availableDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public LocalDate getAvailableDate() {
        return availableDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
