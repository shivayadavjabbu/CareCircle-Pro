package com.carecircle.user_profile_service.parent.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
import java.util.ArrayList;
import com.carecircle.user_profile_service.child.model.Child;

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
	@GeneratedValue
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;
	
	@Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;
	
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

    // ===== Relationships (Cascading) =====

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> children = new ArrayList<>();

    // ===== Extended Location Info =====

    @Column(name = "city_id")
    private UUID cityId;
	
	protected  ParentProfile() {		
	}
	
    public ParentProfile(UUID userId, String userEmail, String fullName, String phoneNumber, String address, UUID cityId) {
        this.userEmail = userEmail;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.userId = userId; 
        this.cityId = cityId;
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

    public UUID getId() {
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
    
	public UUID getUserId() {
		return userId;
	}

    public List<Child> getChildren() {
        return children;
    }

    public UUID getCityId() {
        return cityId;
    }

    public void setCityId(UUID cityId) {
        this.cityId = cityId;
    }
}
