package com.carecircle.matchingBookingService.booking.api.dto;

public class RateBookingRequest {
    
    private Integer rating; // 1-5
    private String review; // optional
    
    public Integer getRating() {
        return rating;
    }
    
    public String getReview() {
        return review;
    }
}
