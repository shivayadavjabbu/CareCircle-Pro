package com.carecircle.user_profile_service.common.controller;

import com.carecircle.user_profile_service.admin.repository.AdminProfileRepository;
import com.carecircle.user_profile_service.caregiver.repository.CaregiverProfileRepository;
import com.carecircle.user_profile_service.common.dto.UserBatchLookupRequest;
import com.carecircle.user_profile_service.common.dto.UserSummaryDto;
import com.carecircle.user_profile_service.common.service.MatchingIntegrationService;
import com.carecircle.user_profile_service.parent.repository.ParentProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller for public lookups (Cities, Services) fetched from Matching Service.
 * Useful for populating dropdowns in the frontend.
 */
@RestController
@RequestMapping("/api/lookup")
public class CommonLookupController {

    private final MatchingIntegrationService matchingService;
    private final ParentProfileRepository parentRepository;
    private final CaregiverProfileRepository caregiverRepository;
    private final AdminProfileRepository adminRepository;

    public CommonLookupController(
            MatchingIntegrationService matchingService,
            ParentProfileRepository parentRepository,
            CaregiverProfileRepository caregiverRepository,
            AdminProfileRepository adminRepository
    ) {
        this.matchingService = matchingService;
        this.parentRepository = parentRepository;
        this.caregiverRepository = caregiverRepository;
        this.adminRepository = adminRepository;
    }

    @GetMapping("/cities")
    public ResponseEntity<List<MatchingIntegrationService.CityDto>> getCities() {
        return ResponseEntity.ok(matchingService.getAllCities());
    }

    @GetMapping("/services")
    public ResponseEntity<List<MatchingIntegrationService.ServiceDto>> getServices() {
        return ResponseEntity.ok(matchingService.getAllServices());
    }

    @PostMapping("/users")
    public ResponseEntity<List<UserSummaryDto>> lookupUsers(@RequestBody UserBatchLookupRequest request) {
        List<UUID> ids = request.getUserIds();
        List<UserSummaryDto> results = new ArrayList<>();

        // Search across all three profile tables
        results.addAll(parentRepository.findByUserIdIn(ids).stream()
                .map(p -> new UserSummaryDto(p.getUserId(), p.getFullName(), "ROLE_PARENT"))
                .collect(Collectors.toList()));

        results.addAll(caregiverRepository.findByUserIdIn(ids).stream()
                .map(c -> new UserSummaryDto(c.getUserId(), c.getFullName(), "ROLE_CARETAKER"))
                .collect(Collectors.toList()));

        results.addAll(adminRepository.findByUserIdIn(ids).stream()
                .map(a -> new UserSummaryDto(a.getUserId(), a.getFullName(), "ROLE_ADMIN"))
                .collect(Collectors.toList()));

        return ResponseEntity.ok(results);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserSummaryDto> lookupUser(@PathVariable UUID userId) {
        UserSummaryDto user = parentRepository.findByUserId(userId)
                .map(p -> new UserSummaryDto(p.getUserId(), p.getFullName(), "ROLE_PARENT"))
                .or(() -> caregiverRepository.findByUserId(userId)
                        .map(c -> new UserSummaryDto(c.getUserId(), c.getFullName(), "ROLE_CARETAKER")))
                .or(() -> adminRepository.findByUserId(userId)
                        .map(a -> new UserSummaryDto(a.getUserId(), a.getFullName(), "ROLE_ADMIN")))
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return ResponseEntity.ok(user);
    }
}
