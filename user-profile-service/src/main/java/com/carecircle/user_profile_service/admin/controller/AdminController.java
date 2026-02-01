package com.carecircle.user_profile_service.admin.controller;

import com.carecircle.user_profile_service.admin.dto.AdminProfileResponse;
import com.carecircle.user_profile_service.common.dto.PagedResponse;
import com.carecircle.user_profile_service.admin.dto.AdminStatisticsResponse;
import com.carecircle.user_profile_service.admin.dto.CaregiverSummaryResponse;
import com.carecircle.user_profile_service.admin.dto.CreateAdminProfileRequest;
import com.carecircle.user_profile_service.admin.dto.DisableRequest;
import com.carecircle.user_profile_service.admin.dto.ParentSummaryResponse;
import com.carecircle.user_profile_service.admin.dto.RejectRequest;
import com.carecircle.user_profile_service.admin.dto.UpdateAdminProfileRequest;
import com.carecircle.user_profile_service.admin.dto.VerifyRequest;
import com.carecircle.user_profile_service.admin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> createAdminProfile(
            @Valid @RequestBody CreateAdminProfileRequest request,
            HttpServletRequest httpRequest
    ) {
    	UUID userId = validateAdminAndGetUserId(httpRequest);
        adminService.createAdminProfile(
                userId,
                httpRequest.getHeader(USER_EMAIL_HEADER),
                request.getFullName(),
                request.getPhoneNumber(),
                request.getAdminLevel(),
                request.getAddress(),
                request.getCity()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/profile")
    public ResponseEntity<AdminProfileResponse> getMyProfile(HttpServletRequest httpRequest) {
    	UUID userId = validateAdminAndGetUserId(httpRequest);
        return ResponseEntity.ok(adminService.getMyProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<AdminProfileResponse> updateMyProfile(
            @Valid @RequestBody UpdateAdminProfileRequest request,
            HttpServletRequest httpRequest
    ) {
        UUID userId = validateAdminAndGetUserId(httpRequest);
        AdminProfileResponse response = adminService.updateMyProfile(
                userId,
                request.getFullName(),
                request.getPhoneNumber(),
                request.getAdminLevel(),
                request.getAddress(),
                request.getCity()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteMyProfile(HttpServletRequest httpRequest) {
        UUID userId = validateAdminAndGetUserId(httpRequest);
        adminService.deleteMyProfile(userId);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // Statistics & Listing
    // =========================

    @GetMapping("/stats")
    public ResponseEntity<AdminStatisticsResponse> getStatistics(HttpServletRequest httpRequest) {
        validateAdminAndGetUserId(httpRequest);
        return ResponseEntity.ok(adminService.getStatistics());
    }

    @GetMapping("/parents")
    public ResponseEntity<PagedResponse<ParentSummaryResponse>> getAllParents(
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest
    ) {
        validateAdminAndGetUserId(httpRequest);
        return ResponseEntity.ok(adminService.getAllParents(city, page, size));
    }

    @GetMapping("/parents/{parentId}/children")
    public ResponseEntity<List<com.carecircle.user_profile_service.child.dto.ChildResponse>> getChildrenForParent(
            @PathVariable UUID parentId,
            HttpServletRequest httpRequest
    ) {
        validateAdminAndGetUserId(httpRequest);
        return ResponseEntity.ok(adminService.getChildrenForParent(parentId));
    }

    @GetMapping("/caregivers")
    public ResponseEntity<PagedResponse<CaregiverSummaryResponse>> getAllCaregivers(
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest
    ) {
        validateAdminAndGetUserId(httpRequest);
        return ResponseEntity.ok(adminService.getAllCaregivers(city, page, size));
    }


    // Caregiver Profile

    @PostMapping("/caregivers/{caregiverId}/verify")
    public ResponseEntity<Void> verifyCaregiverProfile(
            @PathVariable UUID caregiverId,
            @Valid @RequestBody VerifyRequest request,
            HttpServletRequest httpRequest
    ) {
    	UUID userId = validateAdminAndGetUserId(httpRequest);
        adminService.verifyCaregiverProfile(userId, caregiverId, request.getReason());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/caregivers/{caregiverId}/reject")
    public ResponseEntity<Void> rejectCaregiverProfile(
            @PathVariable UUID caregiverId,
            @Valid @RequestBody RejectRequest request,
            HttpServletRequest httpRequest
    ) {
    	UUID userId = validateAdminAndGetUserId(httpRequest);
        adminService.rejectCaregiverProfile(userId, caregiverId, request.getReason());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/caregivers/{caregiverId}/disable")
    public ResponseEntity<Void> disableCaregiverProfile(
            @PathVariable UUID caregiverId,
            @Valid @RequestBody DisableRequest request,
            HttpServletRequest httpRequest
    ) {
    	UUID userId = validateAdminAndGetUserId(httpRequest);
        adminService.disableCaregiverProfile(userId, caregiverId, request.getReason());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/audits/profiles")
    public ResponseEntity<List<com.carecircle.user_profile_service.admin.dto.ProfileVerificationAuditResponse>> getProfileAudits(HttpServletRequest httpRequest) {
        validateAdminAndGetUserId(httpRequest);
        return ResponseEntity.ok(adminService.getProfileAudits());
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
