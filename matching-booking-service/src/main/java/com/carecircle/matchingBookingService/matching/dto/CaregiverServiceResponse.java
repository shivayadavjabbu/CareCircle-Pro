package com.carecircle.matchingBookingService.matching.dto;

import java.util.UUID;

public class CaregiverServiceResponse {
    private UUID id;
    private UUID caregiverId;
    private String caregiverName;
    private String city;
    private UUID serviceId;
    private Double extraPrice;
    private String description;
    private Integer minChildAge;
    private Integer maxChildAge;

    public CaregiverServiceResponse() {}

    public CaregiverServiceResponse(
            UUID id, UUID caregiverId, String caregiverName, String city,
            UUID serviceId, Double extraPrice, String description,
            Integer minChildAge, Integer maxChildAge
    ) {
        this.id = id;
        this.caregiverId = caregiverId;
        this.caregiverName = caregiverName;
        this.city = city;
        this.serviceId = serviceId;
        this.extraPrice = extraPrice;
        this.description = description;
        this.minChildAge = minChildAge;
        this.maxChildAge = maxChildAge;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getCaregiverId() { return caregiverId; }
    public void setCaregiverId(UUID caregiverId) { this.caregiverId = caregiverId; }
    public String getCaregiverName() { return caregiverName; }
    public void setCaregiverName(String caregiverName) { this.caregiverName = caregiverName; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public UUID getServiceId() { return serviceId; }
    public void setServiceId(UUID serviceId) { this.serviceId = serviceId; }
    public Double getExtraPrice() { return extraPrice; }
    public void setExtraPrice(Double extraPrice) { this.extraPrice = extraPrice; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getMinChildAge() { return minChildAge; }
    public void setMinChildAge(Integer minChildAge) { this.minChildAge = minChildAge; }
    public Integer getMaxChildAge() { return maxChildAge; }
    public void setMaxChildAge(Integer maxChildAge) { this.maxChildAge = maxChildAge; }
}
