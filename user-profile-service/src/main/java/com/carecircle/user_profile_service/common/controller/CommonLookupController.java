package com.carecircle.user_profile_service.common.controller;

import com.carecircle.user_profile_service.common.service.MatchingIntegrationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for public lookups (Cities, Services) fetched from Matching Service.
 * Useful for populating dropdowns in the frontend.
 */
@RestController
@RequestMapping("/api/lookup")
public class CommonLookupController {

    private final MatchingIntegrationService matchingService;

    public CommonLookupController(MatchingIntegrationService matchingService) {
        this.matchingService = matchingService;
    }

    @GetMapping("/cities")
    public List<MatchingIntegrationService.CityDto> getCities() {
        return matchingService.getAllCities();
    }

    @GetMapping("/services")
    public List<MatchingIntegrationService.ServiceDto> getServices() {
        return matchingService.getAllServices();
    }
}
