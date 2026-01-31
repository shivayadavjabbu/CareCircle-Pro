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

    @GetMapping("/services/lookup")
    public ServiceEntity getServiceByCode(@RequestParam String code) {
        return serviceRepository.findByCodeIgnoreCaseAndActiveTrue(code)
                .orElseThrow(() -> new RuntimeException("Service not found for code: " + code));
    }

    @GetMapping("/services/lookup-by-name")
    public ServiceEntity getServiceByName(@RequestParam String name) {
        return serviceRepository.findByActiveTrue().stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Service not found for name: " + name));
    }

    @GetMapping("/services/{id}")
    public ServiceEntity getServiceById(@PathVariable java.util.UUID id) {
        return serviceRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Service not found: " + id));
    }

    @GetMapping("/services/id")
    public java.util.UUID getServiceId(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code
    ) {
        if (name != null) {
            return serviceRepository.findByActiveTrue().stream()
                    .filter(s -> s.getName().equalsIgnoreCase(name))
                    .findFirst()
                    .map(ServiceEntity::getId)
                    .orElseThrow(() -> new RuntimeException("Service not found for name: " + name));
        } else if (code != null) {
             return serviceRepository.findByCodeIgnoreCaseAndActiveTrue(code)
                    .map(ServiceEntity::getId)
                    .orElseThrow(() -> new RuntimeException("Service not found for code: " + code));
        } else {
             throw new RuntimeException("Either 'name' or 'code' parameter is required");
        }
    }

    // ADMIN UPDATES & DELETES

    @PutMapping("/admin/services/{id}")
    public ServiceEntity updateService(
            @PathVariable java.util.UUID id,
            @RequestHeader("X-User-Role") String role,
            @RequestBody com.carecircle.matchingBookingService.service.api.dto.UpdateServiceRequest request
    ) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Only admin can update services");
        }

        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found: " + id));

        service.setName(request.getName());
        service.setCategory(request.getCategory());
        service.setBasePrice(request.getBasePrice());

        return serviceRepository.save(service);
    }

    @DeleteMapping("/admin/services/{id}")
    public void deleteService(
            @PathVariable java.util.UUID id,
            @RequestHeader("X-User-Role") String role
    ) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Only admin can delete services");
        }

        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found: " + id));

        service.deactivate();
        serviceRepository.save(service);
    }
}
