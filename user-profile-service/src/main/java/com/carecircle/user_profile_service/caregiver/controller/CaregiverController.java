package com.carecircle.user_profile_service.caregiver.controller;

import com.carecircle.user_profile_service.caregiver.dto.CaregiverProfileResponse;
import com.carecircle.user_profile_service.caregiver.dto.CreateCaregiverProfileRequest;
import com.carecircle.user_profile_service.caregiver.dto.UpdateCaregiverProfileRequest;
import com.carecircle.user_profile_service.caregiver.model.CaregiverProfile;
import com.carecircle.user_profile_service.caregiver.service.CaregiverService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for caregiver self-management APIs.
 *
 * Only accessible by users with CAREGIVER role.
 */
@RestController
@RequestMapping("/caregiver")
public class CaregiverController {

    private static final String USER_EMAIL_HEADER = "X-User-Email";
    private static final String USER_ROLE_HEADER = "X-User-Role";
    private static final String CAREGIVER_ROLE = "ROLE_CARETAKER";
    private static final String USER_ID = "X-User-Id";

    private final CaregiverService caregiverService;

    public CaregiverController(CaregiverService caregiverService) {
        this.caregiverService = caregiverService;
    }

    // =========================
    // Caregiver Profile
    // =========================

    @PostMapping("/profile")
    public ResponseEntity<CaregiverProfileResponse> createProfile(
            @Valid @RequestBody CreateCaregiverProfileRequest request,
            HttpServletRequest httpRequest
    ) {
        validateCaregiverRole(httpRequest);
        String userEmail = extractUserEmail(httpRequest);
        UUID userId = extractUserId(httpRequest);
        

        CaregiverProfile profile = caregiverService.createProfile(
        		userId,
                userEmail,
                request.getFullName(),
                request.getPhoneNumber(),
                request.getAge(),
                request.getGender(),
                request.getAddress(),
                request.getCity(),
                request.getBio(),
                request.getExperienceYears()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mapProfile(profile));
    }

    @GetMapping("/profile")
    public ResponseEntity<CaregiverProfileResponse> getMyProfile(HttpServletRequest httpRequest) {
        validateCaregiverRole(httpRequest);
        UUID userId = extractUserId(httpRequest);

        return ResponseEntity.ok(mapProfile(caregiverService.getMyProfile(userId)));
    }

    @PutMapping("/profile")
    public ResponseEntity<CaregiverProfileResponse> updateMyProfile(
            @Valid @RequestBody UpdateCaregiverProfileRequest request,
            HttpServletRequest httpRequest
    ) {
        validateCaregiverRole(httpRequest);
       UUID userId = extractUserId(httpRequest);

        CaregiverProfile profile = caregiverService.updateMyProfile(
                userId,
                request.getFullName(),
                request.getPhoneNumber(),
                request.getAddress(),
                request.getCity(),
                request.getBio(),
                request.getExperienceYears()
        );

        return ResponseEntity.ok(mapProfile(profile));
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteMyProfile(HttpServletRequest httpRequest) {
        validateCaregiverRole(httpRequest);
        UUID userId = extractUserId(httpRequest);
        caregiverService.deleteProfile(userId);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // Helpers
    // =========================

    private String extractUserEmail(HttpServletRequest request) {
        String email = request.getHeader(USER_EMAIL_HEADER);
        if (email == null || email.isBlank()) {
            throw new RuntimeException("Missing X-User-Email header");
        }
        return email;
    }
    
    private UUID extractUserId(HttpServletRequest request) {
        String userId = request.getHeader(USER_ID);
        if (userId == null || userId.isBlank()) {
            throw new RuntimeException("Missing X-User-Id header");
        }
        return UUID.fromString(userId);
    }

    private void validateCaregiverRole(HttpServletRequest request) {
        String role = request.getHeader(USER_ROLE_HEADER);
        if (!CAREGIVER_ROLE.equals(role)) {
            throw new RuntimeException("Access denied: user is not a caregiver");
        }
    }

    private CaregiverProfileResponse mapProfile(CaregiverProfile p) {
        return new CaregiverProfileResponse(
                p.getId(),
                p.getFullName(),
                p.getUserEmail(), // Added email
                p.getPhoneNumber(),
                p.getAge(),
                p.getGender(),
                p.getAddress(),   // Simplified address
                p.getCity(),
                p.getBio(),
                p.getExperienceYears(),
                p.getVerificationStatus(),
                p.getIsActive()
        );
    }
}

