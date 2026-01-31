package com.carecircle.matchingBookingService.service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "services",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_service_code", columnNames = {"code"})
        }
)
public class ServiceEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "code", nullable = false, updatable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "base_price", nullable = false)
    private Double basePrice;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected ServiceEntity() {
        // JPA only
    }

    public ServiceEntity(String code, String name, String category, Double basePrice) {
        this.code = code;
        this.name = name;
        this.category = category;
        this.basePrice = basePrice;
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

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public Boolean getActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    // Setters for updateable fields
    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }
}
