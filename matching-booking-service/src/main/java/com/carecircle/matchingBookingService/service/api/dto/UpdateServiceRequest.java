package com.carecircle.matchingBookingService.service.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateServiceRequest {

    @NotBlank(message = "Service name is required")
    private String serviceName;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Base price is required")
    private Double basePrice;

    public UpdateServiceRequest() {}

    public String getServiceName() { return serviceName; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public Double getBasePrice() { return basePrice; }

    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setBasePrice(Double basePrice) { this.basePrice = basePrice; }
}
