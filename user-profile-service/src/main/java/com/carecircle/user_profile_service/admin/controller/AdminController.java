package com.carecircle.user_profile_service.admin.controller;

import com.carecircle.user_profile_service.admin.dto.AdminProfileResponse;
import com.carecircle.user_profile_service.admin.dto.CreateAdminProfileRequest;
import com.carecircle.user_profile_service.admin.dto.DisableRequest;
import com.carecircle.user_profile_service.admin.dto.RejectRequest;
import com.carecircle.user_profile_service.admin.dto.VerifyRequest;
import com.carecircle.user_profile_service.admin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller exposing admin-only verification and moderation APIs.
 *
 * All endpoints require ADMIN role and rely on gateway-injected headers
 * for authentication context.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final String USER_EMAIL_HEADER = "X-User-Email";
    private static final String USER_ROLE_HEADER = "X-User-Role";
    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    private static final String USER_ID = "X-User-Id";

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    
    @PostMapping("/profile")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAdminProfile(
            @Valid @RequestBody CreateAdminProfileRequest request,
            HttpServletRequest httpRequest
    ) {
    	UUID userId = validateAdminAndGetUserId(httpRequest);

        adminService.createAdminProfile(
        		userId,
        		httpRequest.getHeader(USER_EMAIL_HEADER),
                request.getFullName(),
                request.getPhoneNumber(),
                request.getAdminLevel()
        );
    }

    @GetMapping("/profile")
    public AdminProfileResponse getMyProfile(HttpServletRequest httpRequest) {
    	UUID userId = validateAdminAndGetUserId(httpRequest);
        return adminService.getMyProfile(userId);
    }


    // Caregiver Profile

    @PostMapping("/caregivers/{caregiverId}/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verifyCaregiverProfile(
            @PathVariable UUID caregiverId,
            @Valid @RequestBody VerifyRequest request,
            HttpServletRequest httpRequest
    ) {
    	UUID userId = validateAdminAndGetUserId(httpRequest);
        adminService.verifyCaregiverProfile(userId, caregiverId, request.getReason());
    }

    @PostMapping("/caregivers/{caregiverId}/reject")
    @ResponseStatus(HttpStatus.OK)
    public void rejectCaregiverProfile(
            @PathVariable UUID caregiverId,
            @Valid @RequestBody RejectRequest request,
            HttpServletRequest httpRequest
    ) {
    	UUID userId = validateAdminAndGetUserId(httpRequest);
        adminService.rejectCaregiverProfile(userId, caregiverId, request.getReason());
    }

    @PostMapping("/caregivers/{caregiverId}/disable")
    @ResponseStatus(HttpStatus.OK)
    public void disableCaregiverProfile(
            @PathVariable UUID caregiverId,
            @Valid @RequestBody DisableRequest request,
            HttpServletRequest httpRequest
    ) {
    	UUID userId = validateAdminAndGetUserId(httpRequest);
        adminService.disableCaregiverProfile(userId, caregiverId, request.getReason());
    }

    // =========================
    // Caregiver Capability
    // =========================

    @PostMapping("/capabilities/{capabilityId}/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verifyCaregiverCapability(
            @PathVariable UUID capabilityId,
            @Valid @RequestBody VerifyRequest request,
            HttpServletRequest httpRequest
    ) {
    	UUID userId = validateAdminAndGetUserId(httpRequest);
        adminService.verifyCaregiverCapability(userId, capabilityId, request.getReason());
    }

    @PostMapping("/capabilities/{capabilityId}/reject")
    @ResponseStatus(HttpStatus.OK)
    public void rejectCaregiverCapability(
            @PathVariable UUID capabilityId,
            @Valid @RequestBody RejectRequest request,
            HttpServletRequest httpRequest
    ) {
    	UUID userId = validateAdminAndGetUserId(httpRequest);
        adminService.rejectCaregiverCapability(userId, capabilityId, request.getReason());
    }

    // =========================
    // Caregiver Certification
    // =========================

    @PostMapping("/certifications/{certificationId}/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verifyCaregiverCertification(
            @PathVariable UUID certificationId,
            @Valid @RequestBody VerifyRequest request,
            HttpServletRequest httpRequest
    ) {
    	UUID userId = validateAdminAndGetUserId(httpRequest);
        adminService.verifyCaregiverCertification(userId, certificationId, request.getReason());
    }

    @PostMapping("/certifications/{certificationId}/reject")
    @ResponseStatus(HttpStatus.OK)
    public void rejectCaregiverCertification(
            @PathVariable UUID certificationId,
            @Valid @RequestBody RejectRequest request,
            HttpServletRequest httpRequest
    ) {
    	UUID userId = validateAdminAndGetUserId(httpRequest);
        adminService.rejectCaregiverCertification(userId, certificationId, request.getReason());
    }

    // =========================
    // Helpers
    // =========================

    private UUID validateAdminAndGetUserId(HttpServletRequest request) {
        String role = request.getHeader(USER_ROLE_HEADER);
        if (!ADMIN_ROLE.equals(role)) {
            throw new RuntimeException("Access denied: admin role required");
        }

        String userId = request.getHeader(USER_ID);
        if (userId == null || userId.isBlank()) {
            throw new RuntimeException("Missing X-User-ID header");
        }
        return UUID.fromString(userId); //Returns the uuid from the string obtained
    }
}
