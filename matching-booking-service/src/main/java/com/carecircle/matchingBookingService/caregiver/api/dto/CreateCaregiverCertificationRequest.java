package com.carecircle.matchingBookingService.caregiver.api.dto;

import java.time.LocalDate;
import java.util.UUID;

public class CreateCaregiverCertificationRequest {
    
    private String name;
    private String issuedBy;
    private LocalDate validTill;
    private UUID serviceId; // Optional

    public String getName() {
        return name;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public LocalDate getValidTill() {
        return validTill;
    }
    
    public UUID getServiceId() {
        return serviceId;
    }
}
