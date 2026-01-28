package com.carecircle.matchingBookingService.service.api;

import com.carecircle.matchingBookingService.service.api.dto.CreateServiceRequest;
import com.carecircle.matchingBookingService.service.model.ServiceEntity;
import com.carecircle.matchingBookingService.service.repository.ServiceRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ServiceController {

    private final ServiceRepository serviceRepository;

    public ServiceController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    // ADMIN ONLY
    @PostMapping("/admin/services")
    public ServiceEntity createService(
            @RequestHeader("X-User-Role") String role,
            @RequestBody CreateServiceRequest request
    ) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Only admin can create services");
        }

        ServiceEntity service = new ServiceEntity(
                request.getCode(),
                request.getName(),
                request.getCategory(),
                request.getBasePrice()
        );

        return serviceRepository.save(service);
    }

    // PUBLIC
    @GetMapping("/services")
    public List<ServiceEntity> getActiveServices() {
        return serviceRepository.findByActiveTrue();
    }
}
