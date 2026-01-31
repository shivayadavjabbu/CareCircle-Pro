package com.carecircle.user_profile_service.admin.dto;

import java.util.UUID;

/**
 * Parent summary with child count for admin listing.
 */
public class ParentSummaryResponse {
    
    private UUID parentId;
    private String fullName;
    private String email;
    private String city;
    private long childrenCount;

    public ParentSummaryResponse(UUID parentId, String fullName, String email, String city, long childrenCount) {
        this.parentId = parentId;
        this.fullName = fullName;
        this.email = email;
        this.city = city;
        this.childrenCount = childrenCount;
    }

    public UUID getParentId() { return parentId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getCity() { return city; }
    public long getChildrenCount() { return childrenCount; }

    public void setParentId(UUID parentId) { this.parentId = parentId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setCity(String city) { this.city = city; }
    public void setChildrenCount(long childrenCount) { this.childrenCount = childrenCount; }
}
