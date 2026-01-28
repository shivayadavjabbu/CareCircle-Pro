package com.carecircle.matchingBookingService.booking.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(
        name = "bookings",
        indexes = {
                @Index(name = "idx_booking_caregiver", columnList = "caregiver_id"),
                @Index(name = "idx_booking_parent", columnList = "parent_id"),
                @Index(name = "idx_booking_status", columnList = "status")
        }
)
public class Booking {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    // ===== Actors =====

    @Column(name = "parent_id", nullable = false)
    private UUID parentId;

    @Column(name = "caregiver_id", nullable = false)
    private UUID caregiverId;

    @Column(name = "service_id", nullable = false)
    private UUID serviceId;

    // ===== Booking Type =====

    @Column(name = "booking_type", nullable = false)
    private String bookingType; // HOURLY / DAILY

    // ===== Time (Hourly) =====

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    // ===== Date (Daily) =====

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    // ===== Pricing Snapshot =====

    @Column(name = "price_per_unit", nullable = false)
    private Double pricePerUnit;

    @Column(name = "total_units", nullable = false)
    private Integer totalUnits;

    @Column(name = "final_price", nullable = false)
    private Double finalPrice;

    // ===== Status =====

    @Column(name = "status", nullable = false)
    private String status; // REQUESTED / ACCEPTED / REJECTED / CANCELLED / COMPLETED

    // ===== Audit =====

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Booking() {
        // JPA only
    }

    public Booking(
            UUID parentId,
            UUID caregiverId,
            UUID serviceId,
            String bookingType,
            Double pricePerUnit,
            Integer totalUnits,
            Double finalPrice
    ) {
        this.parentId = parentId;
        this.caregiverId = caregiverId;
        this.serviceId = serviceId;
        this.bookingType = bookingType;
        this.pricePerUnit = pricePerUnit;
        this.totalUnits = totalUnits;
        this.finalPrice = finalPrice;
        this.status = "REQUESTED";
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ===== Getters =====

    public UUID getId() {
        return id;
    }

    public UUID getParentId() {
        return parentId;
    }

    public UUID getCaregiverId() {
        return caregiverId;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public String getBookingType() {
        return bookingType;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public Integer getTotalUnits() {
        return totalUnits;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public String getStatus() {
        return status;
    }

    // ===== State transitions =====

    public void accept() {
        this.status = "ACCEPTED";
    }

    public void reject() {
        this.status = "REJECTED";
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    public void complete() {
        this.status = "COMPLETED";
    }
}
