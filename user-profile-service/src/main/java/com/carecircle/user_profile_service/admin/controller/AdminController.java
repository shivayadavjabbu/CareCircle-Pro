package com.carecircle.user_profile_service.admin.controller;

import com.carecircle.user_profile_service.admin.dto.AdminProfileResponse;
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

    @PutMapping("/profile")
    public AdminProfileResponse updateMyProfile(
            @Valid @RequestBody UpdateAdminProfileRequest request,
            HttpServletRequest httpRequest
    ) {
        UUID userId = validateAdminAndGetUserId(httpRequest);
        return adminService.updateMyProfile(
                userId,
                request.getFullName(),
                request.getPhoneNumber(),
                request.getAdminLevel()
        );
    }

    @DeleteMapping("/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyProfile(HttpServletRequest httpRequest) {
        UUID userId = validateAdminAndGetUserId(httpRequest);
        adminService.deleteMyProfile(userId);
    }

    // =========================
    // Statistics & Listing
    // =========================

    @GetMapping("/stats")
    public AdminStatisticsResponse getStatistics(HttpServletRequest httpRequest) {
        validateAdminAndGetUserId(httpRequest);
        return adminService.getStatistics();
    }

    @GetMapping("/parents")
    public List<ParentSummaryResponse> getAllParents(HttpServletRequest httpRequest) {
        validateAdminAndGetUserId(httpRequest);
        return adminService.getAllParents();
    }

    @GetMapping("/parents/{parentId}/children")
    public List<com.carecircle.user_profile_service.child.dto.ChildResponse> getChildrenForParent(
            @PathVariable UUID parentId,
            HttpServletRequest httpRequest
    ) {
        validateAdminAndGetUserId(httpRequest);
        return adminService.getChildrenForParent(parentId);
    }

    @GetMapping("/caregivers")
    public List<CaregiverSummaryResponse> getAllCaregivers(HttpServletRequest httpRequest) {
        validateAdminAndGetUserId(httpRequest);
        return adminService.getAllCaregivers();
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
