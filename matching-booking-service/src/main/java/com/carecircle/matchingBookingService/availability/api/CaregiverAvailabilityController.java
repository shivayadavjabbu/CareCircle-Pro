package com.carecircle.matchingBookingService.availability.api;

import com.carecircle.matchingBookingService.availability.api.dto.CreateAvailabilityRequest;
import com.carecircle.matchingBookingService.availability.model.CaregiverAvailability;
import com.carecircle.matchingBookingService.availability.repository.CaregiverAvailabilityRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/caregiver/availability")
public class CaregiverAvailabilityController {

    private final CaregiverAvailabilityRepository availabilityRepository;

    public CaregiverAvailabilityController(CaregiverAvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    // CAREGIVER ONLY
    @PostMapping
    public CaregiverAvailability addAvailability(
            @RequestHeader("X-User-Id") UUID caregiverId,
            @RequestHeader("X-User-Role") String role,
            @RequestBody CreateAvailabilityRequest request
    ) {
        if (!"ROLE_CARETAKER".equals(role)) {
            throw new RuntimeException("Only caregiver can add availability");
        }

        CaregiverAvailability availability =
                new CaregiverAvailability(
                        caregiverId,
                        request.getDayOfWeek(),
                        request.getStartTime(),
                        request.getEndTime()
                );

        return availabilityRepository.save(availability);
    }

    // CAREGIVER VIEW
    @GetMapping
    public List<CaregiverAvailability> getMyAvailability(
            @RequestHeader("X-User-Id") UUID caregiverId,
            @RequestHeader("X-User-Role") String role
    ) {
        if (!"ROLE_CARETAKER".equals(role)) {
            throw new RuntimeException("Only caregiver can view availability");
        }

        return availabilityRepository.findByCaregiverIdAndActiveTrue(caregiverId);
    }
}
