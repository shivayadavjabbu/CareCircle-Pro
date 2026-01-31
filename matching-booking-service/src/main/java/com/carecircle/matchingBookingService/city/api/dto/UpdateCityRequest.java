package com.carecircle.matchingBookingService.city.api.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateCityRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    public UpdateCityRequest() {}

    public String getName() { return name; }
    public String getState() { return state; }
    public String getCountry() { return country; }

    public void setName(String name) { this.name = name; }
    public void setState(String state) { this.state = state; }
    public void setCountry(String country) { this.country = country; }
}
