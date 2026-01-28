package com.carecircle.matchingBookingService.booking.api.dto;

import java.util.UUID;

public class ChildRequest {

    private UUID childId;
    private String childName;
    private Integer age;
    private String specialNeeds;

    public UUID getChildId() {
        return childId;
    }

    public String getChildName() {
        return childName;
    }

    public Integer getAge() {
        return age;
    }

    public String getSpecialNeeds() {
        return specialNeeds;
    }
}
