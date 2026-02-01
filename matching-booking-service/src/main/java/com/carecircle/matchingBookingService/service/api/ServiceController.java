package com.carecircle.matchingBookingService.service.api;

import com.carecircle.matchingBookingService.service.api.dto.CreateServiceRequest;
import com.carecircle.matchingBookingService.service.model.ServiceEntity;
import com.carecircle.matchingBookingService.service.repository.ServiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ServiceController {

    private final ServiceRepository serviceRepository;

    public ServiceController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    // ADMIN ONLY
    @PostMapping("/admin/services")
    public ResponseEntity<ServiceEntity> createService(
            @RequestHeader("X-User-Role") String role,
            @RequestBody CreateServiceRequest request
    ) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Only admin can create services");
        }

        ServiceEntity service = new ServiceEntity(
                request.getServiceName(),
                request.getDescription(),
                request.getCategory(),
                request.getBasePrice()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(serviceRepository.save(service));
    }

    // PUBLIC
    @GetMapping("/services")
    public ResponseEntity<List<ServiceEntity>> getActiveServices() {
        return ResponseEntity.ok(serviceRepository.findByActiveTrue());
    }

    @GetMapping("/services/lookup")
    public ResponseEntity<ServiceEntity> getServiceByServiceName(@RequestParam String serviceName) {
        ServiceEntity service = serviceRepository.findByServiceNameIgnoreCaseAndActiveTrue(serviceName)
                .orElseThrow(() -> new RuntimeException("Service not found for serviceName: " + serviceName));
        return ResponseEntity.ok(service);
    }

    @GetMapping("/services/lookup-by-description")
    public ResponseEntity<ServiceEntity> getServiceByDescription(@RequestParam String description) {
         ServiceEntity service = serviceRepository.findByDescriptionIgnoreCaseAndActiveTrue(description)
                .orElseThrow(() -> new RuntimeException("Service not found for description: " + description));
         return ResponseEntity.ok(service);
    }

    @GetMapping("/services/{id}")
    public ResponseEntity<ServiceEntity> getServiceById(@PathVariable UUID id) {
        ServiceEntity service = serviceRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Service not found: " + id));
        return ResponseEntity.ok(service);
    }

    @GetMapping("/services/id")
    public ResponseEntity<UUID> getServiceId(
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String serviceName
    ) {
        if (description != null) {
            UUID id = serviceRepository.findByDescriptionIgnoreCaseAndActiveTrue(description)
                    .map(ServiceEntity::getId)
                    .orElseThrow(() -> new RuntimeException("Service not found for description: " + description));
            return ResponseEntity.ok(id);
        } else if (serviceName != null) {
             UUID id = serviceRepository.findByServiceNameIgnoreCaseAndActiveTrue(serviceName)
                    .map(ServiceEntity::getId)
                    .orElseThrow(() -> new RuntimeException("Service not found for serviceName: " + serviceName));
             return ResponseEntity.ok(id);
        } else {
             throw new RuntimeException("Either 'description' or 'serviceName' parameter is required");
        }
    }

    // ADMIN UPDATES & DELETES

    @PutMapping("/admin/services/{id}")
    public ResponseEntity<ServiceEntity> updateService(
            @PathVariable UUID id,
            @RequestHeader("X-User-Role") String role,
            @RequestBody com.carecircle.matchingBookingService.service.api.dto.UpdateServiceRequest request
    ) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Only admin can update services");
        }

        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found: " + id));

        service.setDescription(request.getDescription());
        service.setCategory(request.getCategory());
        service.setBasePrice(request.getBasePrice());

        return ResponseEntity.ok(serviceRepository.save(service));
    }

    @DeleteMapping("/admin/services/{id}")
    public ResponseEntity<Void> deleteService(
            @PathVariable UUID id,
            @RequestHeader("X-User-Role") String role
    ) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Only admin can delete services");
        }

        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found: " + id));

        service.deactivate();
        serviceRepository.save(service);
        return ResponseEntity.noContent().build();
    }
}
