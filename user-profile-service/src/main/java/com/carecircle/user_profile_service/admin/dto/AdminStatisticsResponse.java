package com.carecircle.user_profile_service.admin.dto;

/**
 * Statistics response for admin dashboard.
 */
public class AdminStatisticsResponse {
    
    private long totalParents;
    private long totalChildren;
    private long totalCaregivers;

    public AdminStatisticsResponse(long totalParents, long totalChildren, long totalCaregivers) {
        this.totalParents = totalParents;
        this.totalChildren = totalChildren;
        this.totalCaregivers = totalCaregivers;
    }

    public long getTotalParents() { return totalParents; }
    public long getTotalChildren() { return totalChildren; }
    public long getTotalCaregivers() { return totalCaregivers; }

    public void setTotalParents(long totalParents) { this.totalParents = totalParents; }
    public void setTotalChildren(long totalChildren) { this.totalChildren = totalChildren; }
    public void setTotalCaregivers(long totalCaregivers) { this.totalCaregivers = totalCaregivers; }
}
