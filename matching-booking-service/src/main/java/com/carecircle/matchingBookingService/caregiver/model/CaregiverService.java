package com.carecircle.matchingBookingService.caregiver.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "caregiver_services",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_caregiver_service",
                        columnNames = {"caregiver_id", "service_id"}
                )
        }
)
public class CaregiverService {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "caregiver_id", nullable = false)
    private UUID caregiverId;

    @Column(name = "service_id", nullable = false)
    private UUID serviceId;

    @Column(name = "extra_price", nullable = false)
    private Double extraPrice;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected CaregiverService() {
        // JPA only
    }

    public CaregiverService(UUID caregiverId, UUID serviceId, Double extraPrice) {
        this.caregiverId = caregiverId;
        this.serviceId = serviceId;
        this.extraPrice = extraPrice;
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

    public UUID getServiceId() {
        return serviceId;
    }

    public Double getExtraPrice() {
        return extraPrice;
    }

    public Boolean getActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }
}
