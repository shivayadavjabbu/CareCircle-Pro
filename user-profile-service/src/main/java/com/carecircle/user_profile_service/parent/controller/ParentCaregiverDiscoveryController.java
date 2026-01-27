package com.carecircle.user_profile_service.parent.controller;


import com.carecircle.user_profile_service.parent.dto.CaregiverDetailResponse;
import com.carecircle.user_profile_service.parent.dto.CaregiverSummaryResponse;
import com.carecircle.user_profile_service.parent.exception.ParentRoleRequiredException;
import com.carecircle.user_profile_service.parent.service.ParentCaregiverDiscoveryService;
import jakarta.servlet.http.HttpServletRequest;
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
    private static final String PARENT_ROLE = "PARENT";

    private final ParentCaregiverDiscoveryService discoveryService;

    public ParentCaregiverDiscoveryController(
            ParentCaregiverDiscoveryService discoveryService
    ) {
        this.discoveryService = discoveryService;
    }
    
    // List caregivers

    @GetMapping
    public List<CaregiverSummaryResponse> getAllCaregivers(
            HttpServletRequest request
    ) {
        validateParentRole(request);
        return discoveryService.getAllVerifiedCaregivers();
    }

    // Caregiver details

    @GetMapping("/{caregiverId}")
    public CaregiverDetailResponse getCaregiverById(
            @PathVariable UUID caregiverId,
            HttpServletRequest request
    ) {
        validateParentRole(request);
        return discoveryService.getCaregiverById(caregiverId);
    }

    // Helpers
    private void validateParentRole(HttpServletRequest request) {
        String role = request.getHeader(USER_ROLE_HEADER);
        if (!PARENT_ROLE.equals(role)) {
            throw new ParentRoleRequiredException("Access denied: parent role required");
        }
    }
}

