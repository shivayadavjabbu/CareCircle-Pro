package com.carecircle.matchingBookingService.service.api.dto;

public class CreateServiceRequest {

    private String code;
    private String name;
    private String category;
    private Double basePrice;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Double getBasePrice() {
        return basePrice;
    }
}
