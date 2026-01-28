package com.carecircle.matchingBookingService.availability.api.dto;

import java.time.LocalTime;

public class CreateAvailabilityRequest {

    private String dayOfWeek; // MONDAY .. SUNDAY
    private LocalTime startTime;
    private LocalTime endTime;

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
