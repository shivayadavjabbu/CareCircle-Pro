package com.carecircle.user_profile_service.admin.service.impl;


import com.carecircle.user_profile_service.admin.dto.AdminProfileResponse;
import com.carecircle.user_profile_service.admin.exception.*;
import com.carecircle.user_profile_service.admin.model.AdminProfile;
import com.carecircle.user_profile_service.admin.model.VerificationAudit;
import com.carecircle.user_profile_service.admin.repository.AdminProfileRepository;
import com.carecircle.user_profile_service.admin.repository.VerificationAuditRepository;
import com.carecircle.user_profile_service.admin.service.AdminService;
import com.carecircle.user_profile_service.caregiver.model.CaregiverCapability;
import com.carecircle.user_profile_service.caregiver.model.CaregiverCertification;
import com.carecircle.user_profile_service.caregiver.model.CaregiverProfile;
import com.carecircle.user_profile_service.caregiver.repository.CaregiverCapabilityRepository;
import com.carecircle.user_profile_service.caregiver.repository.CaregiverCertificationRepository;
import com.carecircle.user_profile_service.caregiver.repository.CaregiverProfileRepository;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Implementation of admin verification and moderation logic.
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {
	
	

    private final AdminProfileRepository adminProfileRepository;
    private final VerificationAuditRepository auditRepository;

    private final CaregiverProfileRepository caregiverProfileRepository;
    private final CaregiverCapabilityRepository caregiverCapabilityRepository;
    private final CaregiverCertificationRepository caregiverCertificationRepository;
    
    
    @Override
    public void createAdminProfile(
    		UUID userId,
    		String adminEmail, 
            String fullName,
            String phoneNumber,
            String adminLevel
    ) {
        if (adminProfileRepository.findByUserEmail(adminEmail).isPresent()) {
            throw new RuntimeException("Admin profile already exists");
        }

        AdminProfile admin = new AdminProfile(
        		userId, 
        		adminEmail, 
                fullName,
                phoneNumber,
                adminLevel,
                null, null, null, null, null, null
        );

        adminProfileRepository.save(admin);
    }

    @Override
    public AdminProfileResponse getMyProfile(UUID userId) {
        AdminProfile admin = adminProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new AdminProfileNotFoundException(String.valueOf(userId)));

        return new AdminProfileResponse(
                admin.getId(),
                admin.getFullName(),
                admin.getUserEmail(),
                admin.getAdminLevel(),
                admin.getIsActive(),
                admin.getCreatedAt()
        );
    }


    public AdminServiceImpl(
            AdminProfileRepository adminProfileRepository,
            VerificationAuditRepository auditRepository,
            CaregiverProfileRepository caregiverProfileRepository,
            CaregiverCapabilityRepository caregiverCapabilityRepository,
            CaregiverCertificationRepository caregiverCertificationRepository
    ) {
        this.adminProfileRepository = adminProfileRepository;
        this.auditRepository = auditRepository;
        this.caregiverProfileRepository = caregiverProfileRepository;
        this.caregiverCapabilityRepository = caregiverCapabilityRepository;
        this.caregiverCertificationRepository = caregiverCertificationRepository;
    }

    // =========================
    // Helpers
    // =========================

    private AdminProfile loadActiveAdmin(UUID userId) {
        AdminProfile admin = adminProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new AdminProfileNotFoundException(String.valueOf(userId)));

        if (!Boolean.TRUE.equals(admin.getIsActive())) {
            throw new AdminInactiveException(String.valueOf(userId));
        }
        return admin;
    }

    private void saveAudit(
            AdminProfile admin,
            String targetType,
            UUID targetId,
            String action,
            String previousStatus,
            String newStatus,
            String reason
    ) {
        VerificationAudit audit = new VerificationAudit(
                admin,
                targetType,
                targetId,
                action,
                previousStatus,
                newStatus,
                reason
        );
        auditRepository.save(audit);
    }

    // =========================
    // Caregiver Profile
    // =========================

    @Override
    public void verifyCaregiverProfile(UUID userId, UUID caregiverId, String reason) {
        AdminProfile admin = loadActiveAdmin(userId);

        CaregiverProfile profile = caregiverProfileRepository.findById(caregiverId)
                .orElseThrow(() ->
                        new VerificationTargetNotFoundException("CAREGIVER_PROFILE", caregiverId)
                );

        if ("VERIFIED".equals(profile.getVerificationStatus())) {
            throw new InvalidVerificationStateException("Caregiver profile already verified");
        }

        String previous = profile.getVerificationStatus();
        profile.markVerified();

        saveAudit(
                admin,
                "CAREGIVER_PROFILE",
                caregiverId,
                "VERIFY",
                previous,
                "VERIFIED",
                reason
        );
    }

    @Override
    public void rejectCaregiverProfile(UUID userId, UUID caregiverId, String reason) {
        AdminProfile admin = loadActiveAdmin(userId);

        CaregiverProfile profile = caregiverProfileRepository.findById(caregiverId)
                .orElseThrow(() ->
                        new VerificationTargetNotFoundException("CAREGIVER_PROFILE", caregiverId)
                );

        if ("REJECTED".equals(profile.getVerificationStatus())) {
            throw new InvalidVerificationStateException("Caregiver profile already rejected");
        }

        String previous = profile.getVerificationStatus();
        profile.markRejected(reason);
        saveAudit(
                admin,
                "CAREGIVER_PROFILE",
                caregiverId,
                "REJECT",
                previous,
                "REJECTED",
                reason
        );
    }

    @Override
    public void disableCaregiverProfile(UUID userId, UUID caregiverId, String reason) {
        AdminProfile admin = loadActiveAdmin(userId);

        CaregiverProfile profile = caregiverProfileRepository.findById(caregiverId)
                .orElseThrow(() ->
                        new VerificationTargetNotFoundException("CAREGIVER_PROFILE", caregiverId)
                );

        if (!Boolean.TRUE.equals(profile.getIsActive())) {
            throw new InvalidVerificationStateException("Caregiver profile already disabled");
        }

        profile.disable();

        saveAudit(
                admin,
                "CAREGIVER_PROFILE",
                caregiverId,
                "DISABLE",
                "ACTIVE",
                "DISABLED",
                reason
        );
    }

    // =========================
    // Caregiver Capability
    // =========================

    @Override
    public void verifyCaregiverCapability(UUID userId, UUID capabilityId, String reason) {
        AdminProfile admin = loadActiveAdmin(userId);

        CaregiverCapability capability = caregiverCapabilityRepository.findById(capabilityId)
                .orElseThrow(() ->
                        new VerificationTargetNotFoundException("CAREGIVER_CAPABILITY", capabilityId)
                );

        if (Boolean.TRUE.equals(capability.getVerified())) {
            throw new InvalidVerificationStateException("Capability already verified");
        }

        capability.markVerified();

        saveAudit(
                admin,
                "CAREGIVER_CAPABILITY",
                capabilityId,
                "VERIFY",
                "UNVERIFIED",
                "VERIFIED",
                reason
        );
    }

    @Override
    public void rejectCaregiverCapability(UUID userId, UUID capabilityId, String reason) {
        AdminProfile admin = loadActiveAdmin(userId);

        CaregiverCapability capability = caregiverCapabilityRepository.findById(capabilityId)
                .orElseThrow(() ->
                        new VerificationTargetNotFoundException("CAREGIVER_CAPABILITY", capabilityId)
                );

        if (Boolean.FALSE.equals(capability.getVerified())) {
            throw new InvalidVerificationStateException("Capability already unverified");
        }

        capability.markUnverified();

        saveAudit(
                admin,
                "CAREGIVER_CAPABILITY",
                capabilityId,
                "REJECT",
                "VERIFIED",
                "UNVERIFIED",
                reason
        );
    }

    // =========================
    // Caregiver Certification
    // =========================

    @Override
    public void verifyCaregiverCertification(UUID UserId, UUID certificationId, String reason) {
        AdminProfile admin = loadActiveAdmin(UserId);

        CaregiverCertification cert = caregiverCertificationRepository.findById(certificationId)
                .orElseThrow(() ->
                        new VerificationTargetNotFoundException("CAREGIVER_CERTIFICATION", certificationId)
                );

        if (Boolean.TRUE.equals(cert.getVerified())) {
            throw new InvalidVerificationStateException("Certification already verified");
        }

        cert.markVerified();

        saveAudit(
                admin,
                "CAREGIVER_CERTIFICATION",
                certificationId,
                "VERIFY",
                "UNVERIFIED",
                "VERIFIED",
                reason
        );
    }

    @Override
    public void rejectCaregiverCertification(UUID userId, UUID certificationId, String reason) {
        AdminProfile admin = loadActiveAdmin(userId);

        CaregiverCertification cert = caregiverCertificationRepository.findById(certificationId)
                .orElseThrow(() ->
                        new VerificationTargetNotFoundException("CAREGIVER_CERTIFICATION", certificationId)
                );

        if (Boolean.FALSE.equals(cert.getVerified())) {
            throw new InvalidVerificationStateException("Certification already unverified");
        }

        cert.markUnverified();

        saveAudit(
                admin,
                "CAREGIVER_CERTIFICATION",
                certificationId,
                "REJECT",
                "VERIFIED",
                "UNVERIFIED",
                reason
        );
    }
}


