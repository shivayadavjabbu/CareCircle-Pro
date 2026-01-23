package com.carecircle.user_profile_service.caregiver.controller;

import com.carecircle.user_profile_service.caregiver.dto.*;
import com.carecircle.user_profile_service.caregiver.model.CaregiverCapability;
import com.carecircle.user_profile_service.caregiver.model.CaregiverCertification;
import com.carecircle.user_profile_service.caregiver.model.CaregiverProfile;
import com.carecircle.user_profile_service.caregiver.service.CaregiverService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    private static final String CAREGIVER_ROLE = "CAREGIVER";

    private final CaregiverService caregiverService;

    public CaregiverController(CaregiverService caregiverService) {
        this.caregiverService = caregiverService;
    }

    // =========================
    // Caregiver Profile
    // =========================

    @PostMapping("/profile")
    @ResponseStatus(HttpStatus.CREATED)
    public CaregiverProfileResponse createProfile(
            @Valid @RequestBody CreateCaregiverProfileRequest request,
            HttpServletRequest httpRequest
    ) {
        validateCaregiverRole(httpRequest);
        String userEmail = extractUserEmail(httpRequest);

        CaregiverProfile profile = caregiverService.createProfile(
                userEmail,
                request.getFullName(),
                request.getPhoneNumber(),
                request.getAge(),
                request.getGender(),
                request.getAddressLine1(),
                request.getAddressLine2(),
                request.getCity(),
                request.getState(),
                request.getPincode(),
                request.getCountry(),
                request.getBio(),
                request.getExperienceYears()
        );

        return mapProfile(profile);
    }

    @GetMapping("/profile")
    public CaregiverProfileResponse getMyProfile(HttpServletRequest httpRequest) {
        validateCaregiverRole(httpRequest);
        String userEmail = extractUserEmail(httpRequest);

        return mapProfile(caregiverService.getMyProfile(userEmail));
    }

    @PutMapping("/profile")
    public CaregiverProfileResponse updateMyProfile(
            @Valid @RequestBody CreateCaregiverProfileRequest request,
            HttpServletRequest httpRequest
    ) {
        validateCaregiverRole(httpRequest);
        String userEmail = extractUserEmail(httpRequest);

        CaregiverProfile profile = caregiverService.updateMyProfile(
                userEmail,
                request.getFullName(),
                request.getPhoneNumber(),
                request.getAddressLine1(),
                request.getAddressLine2(),
                request.getCity(),
                request.getState(),
                request.getPincode(),
                request.getCountry(),
                request.getBio(),
                request.getExperienceYears()
        );

        return mapProfile(profile);
    }

    // =========================
    // Capabilities
    // =========================

    @PostMapping("/capabilities")
    @ResponseStatus(HttpStatus.CREATED)
    public CaregiverCapabilityResponse addCapability(
            @Valid @RequestBody CreateCaregiverCapabilityRequest request,
            HttpServletRequest httpRequest
    ) {
        validateCaregiverRole(httpRequest);
        String userEmail = extractUserEmail(httpRequest);

        CaregiverCapability capability = caregiverService.addCapability(
                userEmail,
                request.getServiceType(),
                request.getDescription(),
                request.getMinChildAge(),
                request.getMaxChildAge(),
                request.getRequiresCertification()
        );

        return mapCapability(capability);
    }

    @GetMapping("/capabilities")
    public List<CaregiverCapabilityResponse> getMyCapabilities(
            HttpServletRequest httpRequest
    ) {
        validateCaregiverRole(httpRequest);
        String userEmail = extractUserEmail(httpRequest);

        return caregiverService.getMyCapabilities(userEmail)
                .stream()
                .map(this::mapCapability)
                .collect(Collectors.toList());
    }

    // =========================
    // Certifications
    // =========================

    @PostMapping("/certifications")
    @ResponseStatus(HttpStatus.CREATED)
    public CaregiverCertificationResponse addCertification(
            @RequestBody CreateCaregiverCertificationRequest request,
            HttpServletRequest httpRequest
    ) {
        validateCaregiverRole(httpRequest);
        String userEmail = extractUserEmail(httpRequest);

        CaregiverCertification certification = caregiverService.addCertification(
                userEmail,
                request.getCertificationName(),
                request.getIssuedBy(),
                request.getValidTill()
        );

        return mapCertification(certification);
    }

    @GetMapping("/certifications")
    public List<CaregiverCertificationResponse> getMyCertifications(
            HttpServletRequest httpRequest
    ) {
        validateCaregiverRole(httpRequest);
        String userEmail = extractUserEmail(httpRequest);

        return caregiverService.getMyCertifications(userEmail)
                .stream()
                .map(this::mapCertification)
                .collect(Collectors.toList());
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
                p.getPhoneNumber(),
                p.getAge(),
                p.getGender(),
                p.getAddressLine1(),
                p.getAddressLine2(),
                p.getCity(),
                p.getState(),
                p.getPincode(),
                p.getCountry(),
                p.getBio(),
                p.getExperienceYears(),
                p.getVerificationStatus(),
                p.getIsActive(),
                p.getOverallRating(),
                p.getTotalReviews(),
                p.getCreatedAt()
        );
    }

    private CaregiverCapabilityResponse mapCapability(CaregiverCapability c) {
        return new CaregiverCapabilityResponse(
                c.getId(),
                c.getServiceType(),
                c.getDescription(),
                c.getMinChildAge(),
                c.getMaxChildAge(),
                c.getVerified(),
                c.getAverageRating(),
                c.getTotalReviews(),
                c.getCreatedAt()
        );
    }

    private CaregiverCertificationResponse mapCertification(CaregiverCertification c) {
        return new CaregiverCertificationResponse(
                c.getId(),
                c.getCertificationName(),
                c.getIssuedBy(),
                c.getVerified(),
                c.getCreatedAt()
        );
    }
}

