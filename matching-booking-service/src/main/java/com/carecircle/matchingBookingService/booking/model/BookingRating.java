package com.carecircle.matchingBookingService.booking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "booking_ratings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_booking_rating",
                        columnNames = {"booking_id"}
                )
        },
        indexes = {
                @Index(name = "idx_booking_rating_caregiver", columnList = "caregiver_id")
        }
)
public class BookingRating {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "booking_id", nullable = false)
    private UUID bookingId;

    @Column(name = "caregiver_id", nullable = false)
    private UUID caregiverId;

    @Column(name = "parent_id", nullable = false)
    private UUID parentId;

    @Column(name = "rating", nullable = false)
    private Integer rating; // 1-5

    @Column(name = "review", length = 1000)
    private String review;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected BookingRating() {
        // JPA only
    }

    public BookingRating(
            UUID bookingId,
            UUID caregiverId,
            UUID parentId,
            Integer rating,
            String review
    ) {
        this.bookingId = bookingId;
        this.caregiverId = caregiverId;
        this.parentId = parentId;
        this.rating = rating;
        this.review = review;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public UUID getCaregiverId() {
        return caregiverId;
    }

   public UUID getParentId() {
        return parentId;
    }

    public Integer getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
