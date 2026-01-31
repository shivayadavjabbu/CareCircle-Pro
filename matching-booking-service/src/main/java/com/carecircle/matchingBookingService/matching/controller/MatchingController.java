package com.carecircle.matchingBookingService.matching.controller;

import com.carecircle.matchingBookingService.caregiver.model.CaregiverService;
import com.carecircle.matchingBookingService.caregiver.repository.CaregiverServiceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/matching")
public class MatchingController {

    private final CaregiverServiceRepository caregiverServiceRepository;

    public MatchingController(CaregiverServiceRepository caregiverServiceRepository) {
        this.caregiverServiceRepository = caregiverServiceRepository;
    }

    @GetMapping("/search")
    public Page<CaregiverService> searchServices(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) UUID serviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);

        if (city != null && serviceId != null) {
            return caregiverServiceRepository.findByCityAndServiceIdAndActiveTrue(city, serviceId, pageable);
        } else if (city != null) {
            return caregiverServiceRepository.findByCityAndActiveTrue(city, pageable);
        } else if (serviceId != null) {
            return caregiverServiceRepository.findByServiceIdAndActiveTrue(serviceId, pageable);
        } else {
            return caregiverServiceRepository.findByActiveTrue(pageable);
        }
    }
}
