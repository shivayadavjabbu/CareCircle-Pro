package com.carecircle.user_profile_service.admin.controller;

import com.carecircle.user_profile_service.admin.dto.AdminProfileResponse;
import com.carecircle.user_profile_service.admin.dto.CreateAdminProfileRequest;
import com.carecircle.user_profile_service.admin.dto.DisableRequest;
import com.carecircle.user_profile_service.admin.dto.RejectRequest;
import com.carecircle.user_profile_service.admin.dto.VerifyRequest;
import com.carecircle.user_profile_service.admin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
        String adminEmail = validateAdminAndGetEmail(httpRequest);

        adminService.createAdminProfile(
                adminEmail,
                request.getFullName(),
                request.getPhoneNumber(),
                request.getAdminLevel()
        );
    }

    @GetMapping("/profile")
    public AdminProfileResponse getMyProfile(HttpServletRequest httpRequest) {
        String adminEmail = validateAdminAndGetEmail(httpRequest);
        return adminService.getMyProfile(adminEmail);
    }


    // Caregiver Profile

    @PostMapping("/caregivers/{caregiverId}/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verifyCaregiverProfile(
            @PathVariable Long caregiverId,
            @Valid @RequestBody VerifyRequest request,
            HttpServletRequest httpRequest
    ) {
        String adminEmail = validateAdminAndGetEmail(httpRequest);
        adminService.verifyCaregiverProfile(adminEmail, caregiverId, request.getReason());
    }

    @PostMapping("/caregivers/{caregiverId}/reject")
    @ResponseStatus(HttpStatus.OK)
    public void rejectCaregiverProfile(
            @PathVariable Long caregiverId,
            @Valid @RequestBody RejectRequest request,
            HttpServletRequest httpRequest
    ) {
        String adminEmail = validateAdminAndGetEmail(httpRequest);
        adminService.rejectCaregiverProfile(adminEmail, caregiverId, request.getReason());
    }

    @PostMapping("/caregivers/{caregiverId}/disable")
    @ResponseStatus(HttpStatus.OK)
    public void disableCaregiverProfile(
            @PathVariable Long caregiverId,
            @Valid @RequestBody DisableRequest request,
            HttpServletRequest httpRequest
    ) {
        String adminEmail = validateAdminAndGetEmail(httpRequest);
        adminService.disableCaregiverProfile(adminEmail, caregiverId, request.getReason());
    }

    // =========================
    // Caregiver Capability
    // =========================

    @PostMapping("/capabilities/{capabilityId}/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verifyCaregiverCapability(
            @PathVariable Long capabilityId,
            @Valid @RequestBody VerifyRequest request,
            HttpServletRequest httpRequest
    ) {
        String adminEmail = validateAdminAndGetEmail(httpRequest);
        adminService.verifyCaregiverCapability(adminEmail, capabilityId, request.getReason());
    }

    @PostMapping("/capabilities/{capabilityId}/reject")
    @ResponseStatus(HttpStatus.OK)
    public void rejectCaregiverCapability(
            @PathVariable Long capabilityId,
            @Valid @RequestBody RejectRequest request,
            HttpServletRequest httpRequest
    ) {
        String adminEmail = validateAdminAndGetEmail(httpRequest);
        adminService.rejectCaregiverCapability(adminEmail, capabilityId, request.getReason());
    }

    // =========================
    // Caregiver Certification
    // =========================

    @PostMapping("/certifications/{certificationId}/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verifyCaregiverCertification(
            @PathVariable Long certificationId,
            @Valid @RequestBody VerifyRequest request,
            HttpServletRequest httpRequest
    ) {
        String adminEmail = validateAdminAndGetEmail(httpRequest);
        adminService.verifyCaregiverCertification(adminEmail, certificationId, request.getReason());
    }

    @PostMapping("/certifications/{certificationId}/reject")
    @ResponseStatus(HttpStatus.OK)
    public void rejectCaregiverCertification(
            @PathVariable Long certificationId,
            @Valid @RequestBody RejectRequest request,
            HttpServletRequest httpRequest
    ) {
        String adminEmail = validateAdminAndGetEmail(httpRequest);
        adminService.rejectCaregiverCertification(adminEmail, certificationId, request.getReason());
    }

    // =========================
    // Helpers
    // =========================

    private String validateAdminAndGetEmail(HttpServletRequest request) {
        String role = request.getHeader(USER_ROLE_HEADER);
        if (!ADMIN_ROLE.equals(role)) {
            throw new RuntimeException("Access denied: admin role required");
        }

        String email = request.getHeader(USER_EMAIL_HEADER);
        if (email == null || email.isBlank()) {
            throw new RuntimeException("Missing X-User-Email header");
        }
        return email;
    }
}
