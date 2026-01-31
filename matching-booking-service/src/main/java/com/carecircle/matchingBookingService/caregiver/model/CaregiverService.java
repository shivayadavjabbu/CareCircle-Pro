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

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "service_id", nullable = false)
    private UUID serviceId;

    @Column(name = "extra_price", nullable = false)
    private Double extraPrice;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "min_child_age")
    private Integer minChildAge;

    @Column(name = "max_child_age")
    private Integer maxChildAge;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected CaregiverService() {
        // JPA only
    }

    public CaregiverService(
            UUID caregiverId,
            String city,
            UUID serviceId,
            Double extraPrice,
            String description,
            Integer minChildAge,
            Integer maxChildAge
    ) {
        this.caregiverId = caregiverId;
        this.city = city;
        this.serviceId = serviceId;
        this.extraPrice = extraPrice;
        this.description = description;
        this.minChildAge = minChildAge;
        this.maxChildAge = maxChildAge;
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

    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public Double getExtraPrice() {
        return extraPrice;
    }

    public String getDescription() {
        return description;
    }

    public Integer getMinChildAge() {
        return minChildAge;
    }

    public Integer getMaxChildAge() {
        return maxChildAge;
    }

    public Boolean getActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }
    
    public void setExtraPrice(Double extraPrice) {
        this.extraPrice = extraPrice;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMinChildAge(Integer minChildAge) {
        this.minChildAge = minChildAge;
    }

    public void setMaxChildAge(Integer maxChildAge) {
        this.maxChildAge = maxChildAge;
    }
}
