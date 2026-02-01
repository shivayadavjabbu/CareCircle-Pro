package com.carecircle.matchingBookingService.availability.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "caregiver_availability", uniqueConstraints = {
        @UniqueConstraint(name = "uk_caregiver_date_time", columnNames = { "caregiver_id", "available_date",
                "start_time", "end_time" })
})
public class CaregiverAvailability {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "caregiver_id", nullable = false)
    private UUID caregiverId;

    @Column(name = "day_of_week")
    private String dayOfWeek; // MONDAY .. SUNDAY (deprecated, kept for backward compatibility)

    @Column(name = "available_date")
    private LocalDate availableDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected CaregiverAvailability() {
        // JPA only
    }

    public CaregiverAvailability(
            UUID caregiverId,
            String dayOfWeek,
            LocalTime startTime,
            LocalTime endTime) {
        this.caregiverId = caregiverId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.active = true;
    }

    public CaregiverAvailability(
            UUID caregiverId,
            LocalDate availableDate,
            LocalTime startTime,
            LocalTime endTime) {
        this.caregiverId = caregiverId;
        this.availableDate = availableDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.active = true;
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

    public UUID getId() {
        return id;
    }

    public UUID getCaregiverId() {
        return caregiverId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalDate getAvailableDate() {
        return availableDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Boolean getActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }
}
