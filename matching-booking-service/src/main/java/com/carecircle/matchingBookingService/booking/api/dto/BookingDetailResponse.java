package com.carecircle.matchingBookingService.booking.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class BookingDetailResponse {
    
    private UUID id;
    private UUID parentId;
    private String parentName;
    private UUID caregiverId;
    private String caregiverName;
    private UUID serviceId;
    private String bookingType;
    
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;
    
    private Double pricePerUnit;
    private Integer totalUnits;
    private Double finalPrice;
    private String status;
    
    private List<ChildDetail> children;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public BookingDetailResponse(
            UUID id, UUID parentId, String parentName, UUID caregiverId, String caregiverName, 
            UUID serviceId, String bookingType, LocalTime startTime, LocalTime endTime,
            LocalDate startDate, LocalDate endDate, Double pricePerUnit,
            Integer totalUnits, Double finalPrice, String status,
            List<ChildDetail> children, LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.parentId = parentId;
        this.parentName = parentName;
        this.caregiverId = caregiverId;
        this.caregiverName = caregiverName;
        this.serviceId = serviceId;
        this.bookingType = bookingType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pricePerUnit = pricePerUnit;
        this.totalUnits = totalUnits;
        this.finalPrice = finalPrice;
        this.status = status;
        this.children = children;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public static class ChildDetail {
        private UUID childId;
        private String childName;
        private Integer age;
        private String specialNeeds;
        
        public ChildDetail(UUID childId, String childName, Integer age, String specialNeeds) {
            this.childId = childId;
            this.childName = childName;
            this.age = age;
            this.specialNeeds = specialNeeds;
        }
        
        public UUID getChildId() { return childId; }
        public String getChildName() { return childName; }
        public Integer getAge() { return age; }
        public String getSpecialNeeds() { return specialNeeds; }
    }
    
    // Getters
    public UUID getId() { return id; }
    public UUID getParentId() { return parentId; }
    public String getParentName() { return parentName; }
    public UUID getCaregiverId() { return caregiverId; }
    public String getCaregiverName() { return caregiverName; }
    public UUID getServiceId() { return serviceId; }
    public String getBookingType() { return bookingType; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Double getPricePerUnit() { return pricePerUnit; }
    public Integer getTotalUnits() { return totalUnits; }
    public Double getFinalPrice() { return finalPrice; }
    public String getStatus() { return status; }
    public List<ChildDetail> getChildren() { return children; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
