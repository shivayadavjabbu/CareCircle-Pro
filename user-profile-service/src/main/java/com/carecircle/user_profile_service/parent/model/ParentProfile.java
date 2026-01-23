package com.carecircle.user_profile_service.parent.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Represents a parent user's profile in the  CareCircle Platfom
 * 
 * This entity stores domain-specific profile information for a parent. 
 * Authentication and authorization details are handled by auth-service
 * and injected via API gateway 
 */

@Entity
@Table(
		name="parent_profiles", 
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "user_email")
		})

public class ParentProfile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	

	@Column(name="user_email", nullable = false, updatable = false)
	private String userEmail; 
	
	@Column(name="full_name", nullable=false)
	private String fullName;
	
	@Column(name="phone_number", nullable=false)
	private String phoneNumber; 
	
	@Column(name="address", nullable=false)
	private String address; 
	
	@Column(name="created_at", nullable=false, updatable=false)
	private LocalDateTime createdAt; 
	
	@Column(name="updated_at", nullable=false)
	private LocalDateTime updatedAt;
	
	protected  ParentProfile() {		
	}
	
	public ParentProfile(String userEmail, String fullName, String phoneNumber, String address) {
        this.userEmail = userEmail;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
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

    // Getters only – entity should not be freely mutable

    public Long getId() {
        return id;
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

    public String getAddress() {
        return address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
	
}
