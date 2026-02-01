package com.carecircle.user_profile_service.parent.controller;


import com.carecircle.user_profile_service.parent.dto.CaregiverDetailResponse;
import com.carecircle.user_profile_service.parent.dto.CaregiverSummaryResponse;
import com.carecircle.user_profile_service.parent.exception.ParentRoleRequiredException;
import com.carecircle.user_profile_service.parent.service.ParentCaregiverDiscoveryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Read-only APIs for parents to discover verified caregivers.
 *
 * Access is restricted to users with PARENT role.
 */
@RestController
@RequestMapping("/parents/caregivers")
public class ParentCaregiverDiscoveryController {

    private static final String USER_ROLE_HEADER = "X-User-Role";
    private static final String PARENT_ROLE = "ROLE_PARENT";

    private final ParentCaregiverDiscoveryService discoveryService;

    public ParentCaregiverDiscoveryController(
            ParentCaregiverDiscoveryService discoveryService
    ) {
        this.discoveryService = discoveryService;
    }
    
    // List caregivers

    @GetMapping
    public ResponseEntity<List<CaregiverSummaryResponse>> getAllCaregivers(
            HttpServletRequest request
    ) {
        validateParentRole(request);
        return ResponseEntity.ok(discoveryService.getAllVerifiedCaregivers());
    }

    // Caregiver details

    @GetMapping("/{caregiverId}")
    public ResponseEntity<CaregiverDetailResponse> getCaregiverById(
            @PathVariable UUID caregiverId,
            HttpServletRequest request
    ) {
        validateParentRole(request);
        return ResponseEntity.ok(discoveryService.getCaregiverById(caregiverId));
    }

    // Helpers
    private void validateParentRole(HttpServletRequest request) {
        String role = request.getHeader(USER_ROLE_HEADER);
        if (!PARENT_ROLE.equals(role)) {
            throw new ParentRoleRequiredException("Access denied: parent role required");
        }
    }
}
