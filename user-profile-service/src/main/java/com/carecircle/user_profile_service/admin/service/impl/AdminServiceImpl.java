package com.carecircle.user_profile_service.admin.service.impl;

import com.carecircle.user_profile_service.admin.dto.AdminProfileResponse;
import com.carecircle.user_profile_service.admin.exception.*;
import com.carecircle.user_profile_service.admin.model.AdminProfile;
import com.carecircle.user_profile_service.admin.model.VerificationAudit;
import com.carecircle.user_profile_service.admin.repository.AdminProfileRepository;
import com.carecircle.user_profile_service.admin.repository.VerificationAuditRepository;
import com.carecircle.user_profile_service.admin.service.AdminService;
import com.carecircle.user_profile_service.caregiver.model.CaregiverProfile;
import com.carecircle.user_profile_service.caregiver.repository.CaregiverProfileRepository;
import com.carecircle.user_profile_service.parent.repository.ParentProfileRepository;
import com.carecircle.user_profile_service.child.repository.ChildRepository;
import com.carecircle.user_profile_service.admin.dto.ParentSummaryResponse;
import com.carecircle.user_profile_service.admin.dto.CaregiverSummaryResponse;
import com.carecircle.user_profile_service.admin.dto.AdminStatisticsResponse;

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
        private final ParentProfileRepository parentProfileRepository;
        private final ChildRepository childRepository;

        @Override
        public void createAdminProfile(
                        UUID userId,
                        String adminEmail,
                        String fullName,
                        String phoneNumber,
                        String adminLevel) {
                if (adminProfileRepository.findByUserEmail(adminEmail).isPresent()) {
                        throw new RuntimeException("Admin profile already exists");
                }

                AdminProfile admin = new AdminProfile(
                                userId,
                                adminEmail,
                                fullName,
                                phoneNumber,
                                adminLevel,
                                null,
                                null);

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
                                admin.getCreatedAt(),
                                admin.getAddress(),
                                admin.getCity());
        }

        @Override
        public AdminProfileResponse updateMyProfile(
                        UUID userId,
                        String fullName,
                        String phoneNumber,
                        String adminLevel) {
                AdminProfile admin = adminProfileRepository.findByUserId(userId)
                                .orElseThrow(() -> new AdminProfileNotFoundException(String.valueOf(userId)));

                // Update only the fields that should change
                admin.setFullName(fullName);
                admin.setPhoneNumber(phoneNumber);
                admin.setAdminLevel(adminLevel);

                // Save will trigger @PreUpdate to update the updatedAt timestamp
                AdminProfile saved = adminProfileRepository.save(admin);

                return new AdminProfileResponse(
                                saved.getId(),
                                saved.getFullName(),
                                saved.getUserEmail(),
                                saved.getAdminLevel(),
                                saved.getIsActive(),
                                saved.getCreatedAt(),
                                saved.getAddress(),
                                saved.getCity());
        }

        @Override
        public void deleteMyProfile(UUID userId) {
                AdminProfile admin = adminProfileRepository.findByUserId(userId)
                                .orElseThrow(() -> new AdminProfileNotFoundException(String.valueOf(userId)));

                // Delete the admin profile
                adminProfileRepository.delete(admin);
        }

        // =========================
        // Statistics & Listing
        // =========================

        @Override
        public com.carecircle.user_profile_service.admin.dto.AdminStatisticsResponse getStatistics() {
                long totalParents = parentProfileRepository.count();
                long totalChildren = childRepository.count();
                long totalCaregivers = caregiverProfileRepository.count();

                return new com.carecircle.user_profile_service.admin.dto.AdminStatisticsResponse(
                                totalParents,
                                totalChildren,
                                totalCaregivers);
        }

        @Override
        public java.util.List<ParentSummaryResponse> getAllParents() {
                return parentProfileRepository.findAll().stream()
                                .map(parent -> {
                                        long childCount = childRepository.countByParent(parent);
                                        return new ParentSummaryResponse(
                                                        parent.getId(),
                                                        parent.getFullName(),
                                                        parent.getUserEmail(),
                                                        parent.getCity(),
                                                        childCount);
                                })
                                .collect(java.util.stream.Collectors.toList());
        }

        @Override
        public java.util.List<com.carecircle.user_profile_service.child.dto.ChildResponse> getChildrenForParent(
                        UUID parentId) {
                com.carecircle.user_profile_service.parent.model.ParentProfile parent = parentProfileRepository
                                .findById(parentId)
                                .orElseThrow(() -> new RuntimeException("Parent not found"));

                return childRepository.findAllByParent(parent).stream()
                                .map(child -> new com.carecircle.user_profile_service.child.dto.ChildResponse(
                                                child.getId(),
                                                child.getName(),
                                                child.getAge(),
                                                child.getGender(),
                                                child.getSpecialNeeds(),
                                                child.getCreatedAt()))
                                .collect(java.util.stream.Collectors.toList());
        }

        @Override
        public java.util.List<com.carecircle.user_profile_service.admin.dto.CaregiverSummaryResponse> getAllCaregivers() {
                return caregiverProfileRepository.findAll().stream()
                                .map(caregiver -> new com.carecircle.user_profile_service.admin.dto.CaregiverSummaryResponse(
                                                caregiver.getId(),
                                                caregiver.getFullName(),
                                                caregiver.getUserEmail(),
                                                caregiver.getCity(),
                                                caregiver.getVerificationStatus(),
                                                caregiver.getIsActive(),
                                                caregiver.getExperienceYears()))
                                .collect(java.util.stream.Collectors.toList());
        }

        public AdminServiceImpl(
                        AdminProfileRepository adminProfileRepository,
                        VerificationAuditRepository auditRepository,
                        CaregiverProfileRepository caregiverProfileRepository,
                        com.carecircle.user_profile_service.parent.repository.ParentProfileRepository parentProfileRepository,
                        com.carecircle.user_profile_service.child.repository.ChildRepository childRepository) {
                this.adminProfileRepository = adminProfileRepository;
                this.auditRepository = auditRepository;
                this.caregiverProfileRepository = caregiverProfileRepository;
                this.parentProfileRepository = parentProfileRepository;
                this.childRepository = childRepository;
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
                        String reason) {
                VerificationAudit audit = new VerificationAudit(
                                admin,
                                targetType,
                                targetId,
                                action,
                                previousStatus,
                                newStatus,
                                reason);
                auditRepository.save(audit);
        }

        // =========================
        // Caregiver Profile
        // =========================

        @Override
        public void verifyCaregiverProfile(UUID userId, UUID caregiverId, String reason) {
                AdminProfile admin = loadActiveAdmin(userId);

                CaregiverProfile profile = caregiverProfileRepository.findById(caregiverId)
                                .orElseThrow(() -> new VerificationTargetNotFoundException("CAREGIVER_PROFILE",
                                                caregiverId));

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
                                reason);
        }

        @Override
        public void rejectCaregiverProfile(UUID userId, UUID caregiverId, String reason) {
                AdminProfile admin = loadActiveAdmin(userId);

                CaregiverProfile profile = caregiverProfileRepository.findById(caregiverId)
                                .orElseThrow(() -> new VerificationTargetNotFoundException("CAREGIVER_PROFILE",
                                                caregiverId));

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
                                reason);
        }

        @Override
        public void disableCaregiverProfile(UUID userId, UUID caregiverId, String reason) {
                AdminProfile admin = loadActiveAdmin(userId);

                CaregiverProfile profile = caregiverProfileRepository.findById(caregiverId)
                                .orElseThrow(() -> new VerificationTargetNotFoundException("CAREGIVER_PROFILE",
                                                caregiverId));

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
                                reason);
        }
}
