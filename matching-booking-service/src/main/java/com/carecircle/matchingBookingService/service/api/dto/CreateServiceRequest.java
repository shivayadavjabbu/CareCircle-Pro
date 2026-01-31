package com.carecircle.matchingBookingService.service.api.dto;

public class CreateServiceRequest {

    private String serviceName;
    private String description;
    private String category;
    private Double basePrice;

    public String getServiceName() {
        return serviceName;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public Double getBasePrice() {
        return basePrice;
    }
}
