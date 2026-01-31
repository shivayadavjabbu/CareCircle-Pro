package com.carecircle.matchingBookingService.service.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateServiceRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Base price is required")
    private Double basePrice;

    public UpdateServiceRequest() {}

    public String getName() { return name; }
    public String getCategory() { return category; }
    public Double getBasePrice() { return basePrice; }

    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setBasePrice(Double basePrice) { this.basePrice = basePrice; }
}
