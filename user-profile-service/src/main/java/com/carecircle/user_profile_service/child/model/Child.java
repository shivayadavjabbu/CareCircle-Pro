package com.carecircle.user_profile_service.child.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.carecircle.user_profile_service.parent.model.ParentProfile;

/**
 * Represents a child belonging to a parent profile.
 *
 * A child is owned by exactly one parent and cannot exist independently.
 */
@Entity
@Table(name = "children")
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Owning parent of this child.
     * This relationship is mandatory and immutable.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parent_id", nullable = false, updatable = false)
    private ParentProfile parent;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "special_needs")
    private String specialNeeds;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Child() {
        // JPA only
    }

    public Child(
            ParentProfile parent,
            String name,
            Integer age,
            String gender,
            String specialNeeds
    ) {
        this.parent = parent;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.specialNeeds = specialNeeds;
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

    // Getters only — ownership must not be mutable

    public Long getId() {
        return id;
    }

    public ParentProfile getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getSpecialNeeds() {
        return specialNeeds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Controlled updates (no parent change)

    public void updateDetails(
            String name,
            Integer age,
            String gender,
            String specialNeeds
    ) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.specialNeeds = specialNeeds;
    }
}
