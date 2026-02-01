package com.carecircle.matchingBookingService.matching.controller;

import com.carecircle.matchingBookingService.caregiver.model.CaregiverService;
import com.carecircle.matchingBookingService.caregiver.repository.CaregiverServiceRepository;
import com.carecircle.matchingBookingService.common.service.UserIntegrationService;
import com.carecircle.matchingBookingService.matching.dto.CaregiverServiceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/matching")
public class MatchingController {

    private final CaregiverServiceRepository caregiverServiceRepository;
    private final UserIntegrationService userService;

    public MatchingController(
            CaregiverServiceRepository caregiverServiceRepository,
            UserIntegrationService userService
    ) {
        this.caregiverServiceRepository = caregiverServiceRepository;
        this.userService = userService;
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CaregiverServiceResponse>> searchServices(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) UUID serviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);

        Page<CaregiverService> entityPage;
        if (city != null && serviceId != null) {
            entityPage = caregiverServiceRepository.findByCityAndServiceIdAndActiveTrue(city, serviceId, pageable);
        } else if (city != null) {
            entityPage = caregiverServiceRepository.findByCityAndActiveTrue(city, pageable);
        } else if (serviceId != null) {
            entityPage = caregiverServiceRepository.findByServiceIdAndActiveTrue(serviceId, pageable);
        } else {
            entityPage = caregiverServiceRepository.findByActiveTrue(pageable);
        }

        // Enrich with names in batch
        List<UUID> caregiverIds = entityPage.getContent().stream()
                .map(CaregiverService::getCaregiverId)
                .distinct()
                .collect(Collectors.toList());

        Map<UUID, UserIntegrationService.UserSummary> userMap = userService.getUsersInfo(caregiverIds);

        Page<CaregiverServiceResponse> responsePage = entityPage.map(c -> new CaregiverServiceResponse(
                c.getId(),
                c.getCaregiverId(),
                userMap.containsKey(c.getCaregiverId()) ? userMap.get(c.getCaregiverId()).fullName() : "Unknown Caregiver",
                c.getCity(),
                c.getServiceId(),
                c.getExtraPrice(),
                c.getDescription(),
                c.getMinChildAge(),
                c.getMaxChildAge()
        ));

        return ResponseEntity.ok(responsePage);
    }
}
