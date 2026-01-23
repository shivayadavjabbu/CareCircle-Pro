package com.carecircle.user_profile_service.parent.controller;

import com.carecircle.user_profile_service.parent.dto.CreateParentProfileRequest;
import com.carecircle.user_profile_service.parent.dto.ParentProfileResponse;
import com.carecircle.user_profile_service.parent.model.ParentProfile;
import com.carecircle.user_profile_service.parent.service.ParentProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
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
    private static final String PARENT_ROLE = "PARENT";

    private final ParentProfileService parentProfileService;

    public ParentProfileController(ParentProfileService parentProfileService) {
        this.parentProfileService = parentProfileService;
    }

    /**
     * Creates a parent profile for the authenticated user.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParentProfileResponse createProfile(
            @RequestBody CreateParentProfileRequest request,
            HttpServletRequest httpRequest
    ) {
        String userEmail = extractUserEmail(httpRequest);
        validateParentRole(httpRequest);

        ParentProfile profile = parentProfileService.createProfile(
                userEmail,
                request.getFullName(),
                request.getPhoneNumber(),
                request.getAddress()
        );

        return mapToResponse(profile);
    }

    /**
     * Fetches the authenticated parent's profile.
     */
    @GetMapping("/me")
    public ParentProfileResponse getMyProfile(HttpServletRequest httpRequest) {
        String userEmail = extractUserEmail(httpRequest);
        validateParentRole(httpRequest);

        ParentProfile profile =
                parentProfileService.getProfileByUserEmail(userEmail);

        return mapToResponse(profile);
    }


    // Private helper methods

    private String extractUserEmail(HttpServletRequest request) {
        String email = request.getHeader(USER_EMAIL_HEADER);
        if (email == null || email.isBlank()) {
            throw new IllegalStateException("Missing X-User-Email header");
        }
        return email;
    }

    private void validateParentRole(HttpServletRequest request) {
        String role = request.getHeader(USER_ROLE_HEADER);
        if (!PARENT_ROLE.equals(role)) {
            throw new IllegalStateException("Access denied: user is not a parent");
        }
    }

    private ParentProfileResponse mapToResponse(ParentProfile profile) {
        return new ParentProfileResponse(
                profile.getFullName(),
                profile.getPhoneNumber(),
                profile.getAddress(),
                profile.getCreatedAt()
        );
    }
}
