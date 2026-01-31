package com.carecircle.matchingBookingService.caregiver.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "caregiver_ratings",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_caregiver_rating", columnNames = {"caregiver_id"})
    }
)
public class CaregiverRating {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "caregiver_id", nullable = false)
    private UUID caregiverId;

    @Column(name = "overall_rating", nullable = false)
    private Double overallRating = 0.0;

    @Column(name = "total_reviews", nullable = false)
    private Integer totalReviews = 0;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    protected CaregiverRating() {
        // JPA only
    }

    public CaregiverRating(UUID caregiverId) {
        this.caregiverId = caregiverId;
        this.overallRating = 0.0;
        this.totalReviews = 0;
        this.lastUpdated = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getCaregiverId() {
        return caregiverId;
    }

    public Double getOverallRating() {
        return overallRating;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void updateRating(Double newRating, Integer newTotalReviews) {
        this.overallRating = newRating;
        this.totalReviews = newTotalReviews;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public void updateWithNewRating(Double newRating) {
        if (this.totalReviews == 0) {
            this.overallRating = newRating;
            this.totalReviews = 1;
        } else {
            this.overallRating = ((this.overallRating * this.totalReviews) + newRating) / (this.totalReviews + 1);
            this.totalReviews++;
        }
        this.lastUpdated = LocalDateTime.now();
    }
}
