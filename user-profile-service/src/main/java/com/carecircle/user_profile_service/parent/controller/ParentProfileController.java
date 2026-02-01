package com.carecircle.user_profile_service.parent.controller;

import com.carecircle.user_profile_service.parent.dto.CreateParentProfileRequest;
import com.carecircle.user_profile_service.parent.dto.ParentProfileResponse;
import com.carecircle.user_profile_service.parent.dto.UpdateParentProfileRequest;
import com.carecircle.user_profile_service.parent.model.ParentProfile;
import com.carecircle.user_profile_service.parent.service.ParentProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for parent profile operations.
 *
 * Handles HTTP requests and delegates domain logic to the service layer.
 * Authentication and role information is trusted from API Gateway headers.
 */
@RestController
@RequestMapping("/parents/profile")
public class ParentProfileController {

    private static final String USER_EMAIL_HEADER = "X-User-Email";
    private static final String USER_ROLE_HEADER = "X-User-Role";
    private static final String PARENT_ROLE = "ROLE_PARENT";
    private static final String USER_ID = "X-User-Id";

    private final ParentProfileService parentProfileService;

    public ParentProfileController(ParentProfileService parentProfileService) {
        this.parentProfileService = parentProfileService;
    }

    /**
     * Creates a parent profile for the authenticated user.
     */
    @PostMapping
    public ResponseEntity<ParentProfileResponse> createProfile(
    		 @Valid @RequestBody CreateParentProfileRequest request,
            HttpServletRequest httpRequest
    ) {
        String userEmail = extractUserEmail(httpRequest);
        UUID userId = extractUserId(httpRequest);
        validateParentRole(httpRequest);

        
        ParentProfile profile = parentProfileService.createProfile(
        		userId,
                userEmail,
                request.getFullName(),
                request.getPhoneNumber(),
                request.getAddress(),
                request.getCity()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(profile));
    }

    /**
     * Fetches the authenticated parent's profile.
     */
    @GetMapping("/me")
    public ResponseEntity<ParentProfileResponse> getMyProfile(HttpServletRequest httpRequest) {
        String userEmail = extractUserEmail(httpRequest);
        UUID userId = extractUserId(httpRequest);
        validateParentRole(httpRequest);

        ParentProfile profile =
                parentProfileService.getProfileByUserId(userId);

        return ResponseEntity.ok(mapToResponse(profile));
    }

    /**
     * Updates the authenticated parent's profile.
     */
    @PutMapping
    public ResponseEntity<ParentProfileResponse> updateProfile(
            @Valid @RequestBody UpdateParentProfileRequest request,
            HttpServletRequest httpRequest
    ) {
        UUID userId = extractUserId(httpRequest);
        validateParentRole(httpRequest);

        ParentProfile profile = parentProfileService.updateProfile(
                userId,
                request.getFullName(),
                request.getPhoneNumber(),
                request.getAddress(),
                request.getCity()
        );

        return ResponseEntity.ok(mapToResponse(profile));
    }

    /**
     * Deletes the authenticated parent's profile (cascades to children).
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteProfile(HttpServletRequest httpRequest) {
        UUID userId = extractUserId(httpRequest);
        validateParentRole(httpRequest);
        parentProfileService.deleteProfile(userId);
        return ResponseEntity.noContent().build();
    }


    // Private helper methods

    private String extractUserEmail(HttpServletRequest request) {
        String email = request.getHeader(USER_EMAIL_HEADER);
        if (email == null || email.isBlank()) {
            throw new IllegalStateException("Missing X-User-Email header");
        }
        return email;
    }
    
    private UUID extractUserId(HttpServletRequest request) {
        String userId = request.getHeader(USER_ID);
        System.out.println("---- INCOMING HEADERS ----");
     
        if (userId == null || userId.isBlank()) {
            throw new IllegalStateException("Missing X-User-Id header");
        }
        return UUID.fromString(userId);
    }


    private void validateParentRole(HttpServletRequest request) {
        String role = request.getHeader(USER_ROLE_HEADER);
        System.out.println(role);
        if (!PARENT_ROLE.equals(role)) {
            throw new IllegalStateException("Access denied: user is not a parent");
        }
    }

   
    private ParentProfileResponse mapToResponse(ParentProfile profile) {
        return new ParentProfileResponse(
                profile.getFullName(),
                profile.getPhoneNumber(),
                profile.getAddress(),
                profile.getCity(),
                profile.getCreatedAt()
        );
    }
}
