package com.carecircle.user_profile_service.admin.service;

import java.util.List;
import java.util.UUID;

import com.carecircle.user_profile_service.common.dto.PagedResponse;
import com.carecircle.user_profile_service.admin.dto.AdminProfileResponse;
import com.carecircle.user_profile_service.admin.dto.AdminStatisticsResponse;
import com.carecircle.user_profile_service.admin.dto.ParentSummaryResponse;
import com.carecircle.user_profile_service.child.dto.ChildResponse;

/**
 * Service interface for admin verification and moderation operations.
 */
public interface AdminService {

	void createAdminProfile(
			UUID userId,
			String adminEmail,
			String fullName,
			String phoneNumber,
			String adminLevel,
			String address,
			String city);

	AdminProfileResponse getMyProfile(UUID userId);

	AdminProfileResponse updateMyProfile(
			UUID userId,
			String fullName,
			String phoneNumber,
			String adminLevel,
			String address,
			String city);

	void deleteMyProfile(UUID userId);

	// ===== Statistics & Listing =====

	 AdminStatisticsResponse getStatistics();

	PagedResponse<ParentSummaryResponse> getAllParents(
            String city, int page, int size);

	java.util.List<ChildResponse> getChildrenForParent(UUID parentId);

	PagedResponse<com.carecircle.user_profile_service.admin.dto.CaregiverSummaryResponse> getAllCaregivers(
            String city, int page, int size);

	// ===== Caregiver Profile =====

	void verifyCaregiverProfile(UUID userId, UUID caregiverId, String reason);

	void rejectCaregiverProfile(UUID userId, UUID caregiverId, String reason);

	void disableCaregiverProfile(UUID userId, UUID caregiverId, String reason);

	List<com.carecircle.user_profile_service.admin.dto.ProfileVerificationAuditResponse> getProfileAudits();
}
