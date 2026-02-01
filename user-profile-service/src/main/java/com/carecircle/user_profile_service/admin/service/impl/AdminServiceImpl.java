package com.carecircle.user_profile_service.admin.service.impl;

import com.carecircle.user_profile_service.admin.dto.AdminProfileResponse;
import com.carecircle.user_profile_service.admin.exception.*;
import com.carecircle.user_profile_service.admin.model.AdminProfile;
import com.carecircle.user_profile_service.admin.model.ProfileVerificationAudit;
import com.carecircle.user_profile_service.admin.repository.AdminProfileRepository;
import com.carecircle.user_profile_service.admin.repository.ProfileVerificationAuditRepository;
import com.carecircle.user_profile_service.admin.service.AdminService;
import com.carecircle.user_profile_service.caregiver.model.CaregiverProfile;
import com.carecircle.user_profile_service.caregiver.repository.CaregiverProfileRepository;
import com.carecircle.user_profile_service.parent.model.ParentProfile;
import com.carecircle.user_profile_service.parent.repository.ParentProfileRepository;
import com.carecircle.user_profile_service.child.dto.ChildResponse;
import com.carecircle.user_profile_service.child.repository.ChildRepository;
import com.carecircle.user_profile_service.common.exception.CityNotFoundException;
import com.carecircle.user_profile_service.common.service.MatchingIntegrationService;
import com.carecircle.user_profile_service.admin.dto.ParentSummaryResponse;
import com.carecircle.user_profile_service.admin.dto.CaregiverSummaryResponse;
import com.carecircle.user_profile_service.admin.dto.AdminStatisticsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.carecircle.user_profile_service.common.dto.PagedResponse;
import com.carecircle.user_profile_service.admin.dto.ProfileVerificationAuditResponse;
import java.util.stream.Collectors;
import java.util.*;

import java.util.List;
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
        private final ProfileVerificationAuditRepository auditRepository;
        private final MatchingIntegrationService matchingIntegrationService; // Added MatchingService

        private final CaregiverProfileRepository caregiverProfileRepository;
        private final ParentProfileRepository parentProfileRepository;
        private final ChildRepository childRepository;

        @Override
    public void createAdminProfile(
            UUID userId,
            String adminEmail,
            String fullName,
            String phoneNumber,
            String adminLevel,
            String address,
            String city) {
        if (adminProfileRepository.findByUserEmail(adminEmail).isPresent()) {
            throw new RuntimeException("Admin profile already exists");
        }

        // Validate City
        if (city != null && !city.isBlank()) {
        	matchingIntegrationService.getCityByName(city)
                     .orElseThrow(() -> new IllegalArgumentException("City not found: " + city));
        }

        AdminProfile admin = new AdminProfile(
                userId,
                adminEmail,
                fullName,
                phoneNumber,
                adminLevel,
                address,
                city);

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
            String adminLevel,
            String address,
            String city) {
        AdminProfile admin = adminProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new AdminProfileNotFoundException(String.valueOf(userId)));

        // Validate City if changed/provided
        if (city != null && !city.equals(admin.getCity())) {
            if (!city.isBlank()) {
            	matchingIntegrationService.getCityByName(city)
                        .orElseThrow(() -> new CityNotFoundException("City not found: " + city));
            }
            admin.setCity(city);
        }

        // Update other fields
        admin.setFullName(fullName);
        admin.setPhoneNumber(phoneNumber);
        admin.setAdminLevel(adminLevel);
        admin.setAddress(address);

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
    public AdminStatisticsResponse getStatistics() {
            long totalParents = parentProfileRepository.count();
            long totalChildren = childRepository.count();
            long totalCaregivers = caregiverProfileRepository.count();

            return new AdminStatisticsResponse(
                            totalParents,
                            totalChildren,
                            totalCaregivers);
    }


    @Override
    public PagedResponse<ParentSummaryResponse> getAllParents(String city, int page, int size) {
        if (city != null && !city.isBlank()) {
            matchingIntegrationService.getCityByName(city)
                    .orElseThrow(() -> new CityNotFoundException("City not found: " + city));
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ParentProfile> parentsPage;

        if (city != null && !city.isBlank()) {
            parentsPage = parentProfileRepository.findByCityIgnoreCase(city, pageable);
        } else {
            parentsPage = parentProfileRepository.findAll(pageable);
        }

        List<ParentSummaryResponse> content = parentsPage.getContent().stream()
                .map(parent -> {
                    long childCount = childRepository.countByParent(parent);
                    return new ParentSummaryResponse(
                            parent.getId(),
                            parent.getFullName(),
                            parent.getUserEmail(),
                            parent.getCity(),
                            childCount);
                })
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                parentsPage.getNumber(),
                parentsPage.getSize(),
                parentsPage.getTotalElements(),
                parentsPage.getTotalPages(),
                parentsPage.isLast());
    }

    @Override
    public java.util.List<ChildResponse> getChildrenForParent(
            UUID parentId) {
        ParentProfile parent = parentProfileRepository
                .findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found"));

        return childRepository.findAllByParent(parent).stream()
                .map(child -> new ChildResponse(
                        child.getId(),
                        child.getName(),
                        child.getAge(),
                        child.getGender(),
                        child.getSpecialNeeds(),
                        child.getCreatedAt()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public PagedResponse<CaregiverSummaryResponse> getAllCaregivers(String city, int page, int size) {
        if (city != null && !city.isBlank()) {
            matchingIntegrationService.getCityByName(city)
                    .orElseThrow(() -> new CityNotFoundException("City not found: " + city));
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<CaregiverProfile> caregiversPage;

        if (city != null && !city.isBlank()) {
            caregiversPage = caregiverProfileRepository.findByCityIgnoreCase(city, pageable);
        } else {
            caregiversPage = caregiverProfileRepository.findAll(pageable);
        }

        List<CaregiverSummaryResponse> content = caregiversPage.getContent().stream()
                .map(caregiver -> new CaregiverSummaryResponse(
                        caregiver.getId(),
                        caregiver.getFullName(),
                        caregiver.getUserEmail(),
                        caregiver.getCity(),
                        caregiver.getVerificationStatus(),
                        caregiver.getIsActive(),
                        caregiver.getExperienceYears()))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                caregiversPage.getNumber(),
                caregiversPage.getSize(),
                caregiversPage.getTotalElements(),
                caregiversPage.getTotalPages(),
                caregiversPage.isLast());
    }

        public AdminServiceImpl(
                        AdminProfileRepository adminProfileRepository,
                        ProfileVerificationAuditRepository auditRepository,
                        CaregiverProfileRepository caregiverProfileRepository,
                        ParentProfileRepository parentProfileRepository,
                        ChildRepository childRepository, 
                        MatchingIntegrationService matchingIntegrationService
                        ) {
                this.adminProfileRepository = adminProfileRepository;
                this.auditRepository = auditRepository;
                this.caregiverProfileRepository = caregiverProfileRepository;
                this.parentProfileRepository = parentProfileRepository;
                this.childRepository = childRepository;
                this.matchingIntegrationService = matchingIntegrationService;
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
                ProfileVerificationAudit audit = new ProfileVerificationAudit(
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

        @Override
        public List<ProfileVerificationAuditResponse> getProfileAudits() {
                List<ProfileVerificationAudit> audits = auditRepository.findAll();
                if (audits.isEmpty()) return Collections.emptyList();

                // Group target IDs by type to fetch names in batch
                Set<UUID> caregiverIds = audits.stream()
                        .filter(a -> "CAREGIVER_PROFILE".equals(a.getTargetType()))
                        .map(ProfileVerificationAudit::getTargetId)
                        .collect(Collectors.toSet());

                Map<UUID, String> caregiverNames = caregiverProfileRepository.findAllById(caregiverIds).stream()
                        .collect(Collectors.toMap(CaregiverProfile::getId, CaregiverProfile::getFullName));

                return audits.stream().map(a -> {
                        String caregiverName = "Unknown";
                        if ("CAREGIVER_PROFILE".equals(a.getTargetType())) {
                                caregiverName = caregiverNames.getOrDefault(a.getTargetId(), "Unknown Caregiver");
                        }

                        return new ProfileVerificationAuditResponse(
                                a.getId(),
                                a.getAdmin().getUserId(),
                                a.getAdmin().getFullName(),
                                a.getTargetType(),
                                a.getTargetId(),
                                caregiverName,
                                a.getAction(),
                                a.getPreviousStatus(),
                                a.getNewStatus(),
                                a.getReason(),
                                a.getCreatedAt()
                        );
                }).sorted(Comparator.comparing(ProfileVerificationAuditResponse::getCreatedAt).reversed()).toList();
        }
}
