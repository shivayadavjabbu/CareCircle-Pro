package com.carecircle.user_profile_service.admin.service;

/**
 * Service interface for admin verification and moderation operations.
 */
public interface AdminService {

    // ===== Caregiver Profile =====

    void verifyCaregiverProfile(String adminEmail, Long caregiverId, String reason);

    void rejectCaregiverProfile(String adminEmail, Long caregiverId, String reason);

    void disableCaregiverProfile(String adminEmail, Long caregiverId, String reason);

    // ===== Caregiver Capability =====

    void verifyCaregiverCapability(String adminEmail, Long capabilityId, String reason);

    void rejectCaregiverCapability(String adminEmail, Long capabilityId, String reason);

    // ===== Caregiver Certification =====

    void verifyCaregiverCertification(String adminEmail, Long certificationId, String reason);

    void rejectCaregiverCertification(String adminEmail, Long certificationId, String reason);
}
