package com.carecircle.matchingBookingService.caregiver.api.dto;

import java.util.UUID;

public class CreateCaregiverServiceRequest {

    private UUID serviceId;
    private Double extraPrice;
    private String description;
    private Integer minChildAge;
    private Integer maxChildAge;
    private String city;

    public UUID getServiceId() {
        return serviceId;
    }

    public Double getExtraPrice() {
        return extraPrice;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Integer getMinChildAge() {
        return minChildAge;
    }
    
    public Integer getMaxChildAge() {
        return maxChildAge;
    }

    public String getCity() {
        return city;
    }
}
