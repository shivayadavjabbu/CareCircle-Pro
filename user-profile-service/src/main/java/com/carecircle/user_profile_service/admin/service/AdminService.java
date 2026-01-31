package com.carecircle.user_profile_service.admin.service;

import java.util.List;
import java.util.UUID;

import com.carecircle.user_profile_service.admin.dto.AdminProfileResponse;

/**
 * Service interface for admin verification and moderation operations.
 */
public interface AdminService {

	void createAdminProfile(
			UUID userId,
			String adminEmail,
			String fullName,
			String phoneNumber,
			String adminLevel);

	AdminProfileResponse getMyProfile(UUID userId);

	AdminProfileResponse updateMyProfile(
			UUID userId,
			String fullName,
			String phoneNumber,
			String adminLevel);

	void deleteMyProfile(UUID userId);

	// ===== Statistics & Listing =====

	com.carecircle.user_profile_service.admin.dto.AdminStatisticsResponse getStatistics();

	List<com.carecircle.user_profile_service.admin.dto.ParentSummaryResponse> getAllParents();

	List<com.carecircle.user_profile_service.child.dto.ChildResponse> getChildrenForParent(UUID parentId);

	List<com.carecircle.user_profile_service.admin.dto.CaregiverSummaryResponse> getAllCaregivers();

	// ===== Caregiver Profile =====

	void verifyCaregiverProfile(UUID userId, UUID caregiverId, String reason);

	void rejectCaregiverProfile(UUID userId, UUID caregiverId, String reason);

	void disableCaregiverProfile(UUID userId, UUID caregiverId, String reason);
}
