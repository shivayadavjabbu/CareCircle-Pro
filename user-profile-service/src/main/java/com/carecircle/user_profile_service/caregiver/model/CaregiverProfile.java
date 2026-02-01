package com.carecircle.user_profile_service.caregiver.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a caregiver offering childcare-related services.
 *
 * A caregiver can provide multiple services (capabilities) and hold
 * multiple certifications. Visibility to parents depends on verification
 * status and active flag.
 *
 * Rating fields are aggregated and read-only, updated by the system.
 */
@Entity
@Table(name = "caregiver_profiles")
public class CaregiverProfile {

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

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "gender", nullable = false)
    private String gender; // MALE / FEMALE / OTHER / PREFER_NOT_TO_SAY

    // ===== Address (mandatory) =====

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", nullable = false)
    private String city;

    // ===== Professional Info =====

    @Column(name = "bio", length = 1000)
    private String bio;

    @Column(name = "experience_years")
    private Integer experienceYears;

    // ===== Verification & Control =====

    @Column(name = "verification_status", nullable = false)
    private String verificationStatus; // PENDING / VERIFIED / REJECTED

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    // ===== Audit =====

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected CaregiverProfile() {
        // JPA only
    }

    public CaregiverProfile(
            UUID userId,
            String userEmail,
            String fullName,
            String phoneNumber,
            Integer age,
            String gender,
            String address,
            String city,
            String bio,
            Integer experienceYears) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.city = city;
        this.bio = bio;
        this.experienceYears = experienceYears;
        this.verificationStatus = "PENDING";
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

    // ===== Getters only (mutations controlled via service) =====

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
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

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getBio() {
        return bio;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ===== Verification state changes (admin-only usage) =====

    public void markVerified() {
        this.verificationStatus = "VERIFIED";
        this.rejectionReason = null;
    }

    public void markRejected(String reason) {
        this.verificationStatus = "REJECTED";
        this.rejectionReason = reason;
    }

    public void disable() {
        this.isActive = false;
    }

    public void enable() {
        this.isActive = true;
    }

    // ===== Setters for updateable profile fields =====

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

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
