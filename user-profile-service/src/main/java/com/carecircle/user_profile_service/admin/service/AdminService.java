package com.carecircle.user_profile_service.admin.service;

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
	        String adminLevel
	);

	AdminProfileResponse getMyProfile(UUID userId);

    // ===== Caregiver Profile =====

    void verifyCaregiverProfile(UUID userId, UUID caregiverId, String reason);

    void rejectCaregiverProfile(UUID userId, UUID caregiverId, String reason);

    void disableCaregiverProfile(UUID userId, UUID caregiverId, String reason);

    // ===== Caregiver Capability =====

    void verifyCaregiverCapability(UUID userId, UUID capabilityId, String reason);

    void rejectCaregiverCapability(UUID userId, UUID capabilityId, String reason);

    // ===== Caregiver Certification =====

    void verifyCaregiverCertification(UUID userId, UUID certificationId, String reason);

    void rejectCaregiverCertification(UUID userId, UUID certificationId, String reason);
}
