package com.carecircle.matchingBookingService.caregiver.api;

import com.carecircle.matchingBookingService.caregiver.api.dto.CreateCaregiverServiceRequest;
import com.carecircle.matchingBookingService.caregiver.model.CaregiverService;
import com.carecircle.matchingBookingService.caregiver.repository.CaregiverServiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/caregiver")
public class CaregiverServiceController {

    private final CaregiverServiceRepository caregiverServiceRepository;
    private final com.carecircle.matchingBookingService.caregiver.repository.CaregiverCertificationRepository certificationRepository;
    private final com.carecircle.matchingBookingService.caregiver.repository.CaregiverRatingRepository ratingRepository;

    public CaregiverServiceController(
            CaregiverServiceRepository caregiverServiceRepository,
            com.carecircle.matchingBookingService.caregiver.repository.CaregiverCertificationRepository certificationRepository,
            com.carecircle.matchingBookingService.caregiver.repository.CaregiverRatingRepository ratingRepository
    ) {
        this.caregiverServiceRepository = caregiverServiceRepository;
        this.certificationRepository = certificationRepository;
        this.ratingRepository = ratingRepository;
    }

    // ===========================
    // Services (Capabilities)
    // ===========================

    @PostMapping("/services")
    public ResponseEntity<CaregiverService> addService(
            @RequestHeader("X-User-Id") UUID caregiverId,
            @RequestHeader("X-User-Role") String role,
            @RequestBody CreateCaregiverServiceRequest request
    ) {
        if (!"ROLE_CARETAKER".equals(role)) {
            throw new RuntimeException("Only caregiver can add services");
        }

        CaregiverService caregiverService =
                new CaregiverService(
                        caregiverId,
                        request.getCity(),
                        request.getServiceId(),
                        request.getExtraPrice(),
                        request.getDescription(),
                        request.getMinChildAge(),
                        request.getMaxChildAge()
                );

        // Check verification status if certification exists
        certificationRepository.findByCaregiverIdAndServiceId(caregiverId, request.getServiceId())
                .ifPresent(cert -> {
                    if (!"VERIFIED".equals(cert.getVerificationStatus())) {
                        caregiverService.deactivate();
                    }
                });

        return ResponseEntity.status(HttpStatus.CREATED).body(caregiverServiceRepository.save(caregiverService));
    }

    @GetMapping("/services")
    public ResponseEntity<List<CaregiverService>> getMyServices(
            @RequestHeader("X-User-Id") UUID caregiverId,
            @RequestHeader("X-User-Role") String role
    ) {
        return ResponseEntity.ok(caregiverServiceRepository.findByCaregiverIdAndActiveTrue(caregiverId));
    }
    
    // ===========================
    // Certifications
    // ===========================

    @PostMapping("/certifications")
    public ResponseEntity<com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification> addCertification(
            @RequestHeader("X-User-Id") UUID caregiverId,
            @RequestHeader("X-User-Role") String role,
            @RequestBody com.carecircle.matchingBookingService.caregiver.api.dto.CreateCaregiverCertificationRequest request
    ) {
        if (!"ROLE_CARETAKER".equals(role)) {
            throw new RuntimeException("Only caregiver can add certifications");
        }

        com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification certification =
                new com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification(
                        caregiverId,
                        request.getServiceId(),
                        request.getName(),
                        request.getIssuedBy(),
                        request.getValidTill()
                );

        com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification savedCert = 
                certificationRepository.save(certification);

        // If service exists for this certification, deactivate it until verified
        if (request.getServiceId() != null) {
            caregiverServiceRepository.findByCaregiverIdAndServiceId(caregiverId, request.getServiceId())
                    .ifPresent(service -> {
                        service.deactivate();
                        caregiverServiceRepository.save(service);
                    });
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCert);
    }

    @GetMapping("/certifications")
    public ResponseEntity<List<com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification>> getMyCertifications(
            @RequestHeader("X-User-Id") UUID caregiverId
    ) {
        return ResponseEntity.ok(certificationRepository.findByCaregiverId(caregiverId));
    }
    
    // ===========================
    // Ratings
    // ===========================

    @GetMapping("/rating")
    public ResponseEntity<com.carecircle.matchingBookingService.caregiver.model.CaregiverRating> getMyRating(
            @RequestHeader("X-User-Id") UUID caregiverId
    ) {
        // Return empty/default if not present
        return ResponseEntity.ok(ratingRepository.findByCaregiverId(caregiverId)
                .orElse(new com.carecircle.matchingBookingService.caregiver.model.CaregiverRating(caregiverId)));
    }

    @PutMapping("/services")
    public ResponseEntity<CaregiverService> updateService(
            @RequestHeader("X-User-Id") UUID caregiverId,
            @RequestHeader("X-User-Role") String role,
            @RequestBody CreateCaregiverServiceRequest request 
    ) {
        validateCaregiver(role);
        
        CaregiverService service = caregiverServiceRepository.findByCaregiverIdAndServiceId(caregiverId, request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found for this caregiver"));

        service.setDescription(request.getDescription());
        service.setExtraPrice(request.getExtraPrice());
        service.setMinChildAge(request.getMinChildAge());
        service.setMaxChildAge(request.getMaxChildAge());
        if(request.getCity() != null) {
            service.setCity(request.getCity());
        }

        certificationRepository.findByCaregiverIdAndServiceId(caregiverId, request.getServiceId())
                .ifPresent(cert -> {
                    if (!"VERIFIED".equals(cert.getVerificationStatus())) {
                        service.deactivate();
                    }
                });
        
        return ResponseEntity.ok(caregiverServiceRepository.save(service));
    }

    @DeleteMapping("/services/{serviceTypeId}")
    public ResponseEntity<Void> deleteService(
            @RequestHeader("X-User-Id") UUID caregiverId,
            @RequestHeader("X-User-Role") String role,
            @PathVariable UUID serviceTypeId
    ) {
        validateCaregiver(role);
        CaregiverService service = caregiverServiceRepository.findByCaregiverIdAndServiceId(caregiverId, serviceTypeId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        caregiverServiceRepository.delete(service);
        return ResponseEntity.noContent().build();
    }
    
    // Updates
    
    @PutMapping("/certifications/{id}")
    public ResponseEntity<com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification> updateCertification(
            @RequestHeader("X-User-Id") UUID caregiverId,
            @RequestHeader("X-User-Role") String role,
            @PathVariable UUID id,
            @RequestBody com.carecircle.matchingBookingService.caregiver.api.dto.CreateCaregiverCertificationRequest request
    ) {
        validateCaregiver(role);
        com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification cert = certificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certification not found"));
        
        if (!cert.getCaregiverId().equals(caregiverId)) {
            throw new RuntimeException("Access denied");
        }

        cert.setName(request.getName());
        cert.setIssuedBy(request.getIssuedBy());
        cert.setValidTill(request.getValidTill());
        cert.setVerificationStatus("PENDING");
        
        // Lock Service
        if (cert.getServiceId() != null) {
            caregiverServiceRepository.findByCaregiverIdAndServiceId(caregiverId, cert.getServiceId())
                    .ifPresent(service -> {
                        service.deactivate();
                        caregiverServiceRepository.save(service);
                    });
        }
        
        return ResponseEntity.ok(certificationRepository.save(cert));
    }

    @DeleteMapping("/certifications/{id}")
    public ResponseEntity<Void> deleteCertification(
            @RequestHeader("X-User-Id") UUID caregiverId,
            @RequestHeader("X-User-Role") String role,
            @PathVariable UUID id
    ) {
        validateCaregiver(role);
        com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification cert = certificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certification not found"));
        
        if (!cert.getCaregiverId().equals(caregiverId)) {
            throw new RuntimeException("Access denied");
        }
        
        // Deactivate linked service before delete
        if (cert.getServiceId() != null) {
            caregiverServiceRepository.findByCaregiverIdAndServiceId(caregiverId, cert.getServiceId())
                .ifPresent(service -> {
                    service.deactivate();
                    caregiverServiceRepository.save(service);
                });
        }
        
        certificationRepository.delete(cert);
        return ResponseEntity.noContent().build();
    }

    // Cleanup Endpoint
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteCaregiverData(
            @RequestHeader("X-User-Id") UUID caregiverId,
             @RequestHeader("X-User-Role") String role
    ) {
         validateCaregiver(role);
         
         // Delete all certifications
         List<com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification> certs = certificationRepository.findByCaregiverId(caregiverId);
         certificationRepository.deleteAll(certs);
         
         // Delete all services
         List<CaregiverService> services = caregiverServiceRepository.findByCaregiverId(caregiverId);
         caregiverServiceRepository.deleteAll(services);
         return ResponseEntity.noContent().build();
    }

    // Helpers
    private void validateCaregiver(String role) {
        if (!"ROLE_CARETAKER".equals(role)) {
            throw new RuntimeException("Only caregiver can perform this action");
        }
    }
}
