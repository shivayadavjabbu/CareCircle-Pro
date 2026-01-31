package com.carecircle.user_profile_service.admin.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents an administrator of the CareCircle platform.
 *
 * AdminProfile is a first-class domain entity used for accountability
 * and auditing of verification and moderation actions.
 */
@Entity
@Table(name = "admin_profiles")
public class AdminProfile {

	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

    // ===== Identity (linked logically to auth-service) =====

    @Column(name = "user_email", nullable = false, unique = true, updatable = false)
    private String userEmail;
    
    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "admin_level", nullable = false)
    private String adminLevel; // SUPER_ADMIN / ADMIN / SUPPORT

    // ===== Address (optional) =====

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    // ===== Status =====

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // ===== Audit =====

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected AdminProfile() {
        // JPA only
    }

    public AdminProfile(
    		UUID userId, 
            String userEmail,
            String fullName,
            String phoneNumber,
            String adminLevel,
            String address,
            String city
    ) {
    	this.userId = userId; 
        this.userEmail = userEmail;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.adminLevel = adminLevel;
        this.address = address;
        this.city = city;
        this.isActive = true;
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

    // ===== Getters only =====

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getUserEmail() {
        return userEmail;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAdminLevel() {
        return adminLevel;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ===== Setters for updateable fields =====

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
