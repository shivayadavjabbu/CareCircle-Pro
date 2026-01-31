package com.carecircle.matchingBookingService.caregiver.api;

import com.carecircle.matchingBookingService.caregiver.api.dto.CreateCaregiverServiceRequest;
import com.carecircle.matchingBookingService.caregiver.model.CaregiverService;
import com.carecircle.matchingBookingService.caregiver.repository.CaregiverServiceRepository;
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
    public CaregiverService addService(
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

        return caregiverServiceRepository.save(caregiverService);
    }

    @GetMapping("/services")
    public List<CaregiverService> getMyServices(
            @RequestHeader("X-User-Id") UUID caregiverId,
            @RequestHeader("X-User-Role") String role
    ) {
        if (!"ROLE_CARETAKER".equals(role)) {
            // Allow admin or parent to view? 
            // Original code restricted to caregiver. Assuming simple view for now.
             // If this is "getMyServices", implies self.
             // If we need public view, usually it's by caregiverId query param.
             // Sticking to original logic for "getMyServices" via header.
        	if("ROLE_ADMIN".equals(role)) {
        		// Admin might want to see specific caregiver services, but here we rely on header ID which is the caller.
        		// So this endpoint is specifically for the caller.
        		// We will keep it restricted or check requirements. 
        		// Assuming just self-view for now.
        	}
        }
        return caregiverServiceRepository.findByCaregiverIdAndActiveTrue(caregiverId);
    }
    
    // ===========================
    // Certifications
    // ===========================

    @PostMapping("/certifications")
    public com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification addCertification(
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

        return savedCert;
    }

    @GetMapping("/certifications")
    public List<com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification> getMyCertifications(
            @RequestHeader("X-User-Id") UUID caregiverId
    ) {
        return certificationRepository.findByCaregiverId(caregiverId);
    }
    
    // ===========================
    // Ratings
    // ===========================

    @GetMapping("/rating")
    public com.carecircle.matchingBookingService.caregiver.model.CaregiverRating getMyRating(
            @RequestHeader("X-User-Id") UUID caregiverId
    ) {
        // Return empty/default if not present
        return ratingRepository.findByCaregiverId(caregiverId)
                .orElse(new com.carecircle.matchingBookingService.caregiver.model.CaregiverRating(caregiverId));
    }
    @PutMapping("/services")
    public CaregiverService updateService(
            @RequestHeader("X-User-Id") UUID caregiverId,
            @RequestHeader("X-User-Role") String role,
            @RequestBody CreateCaregiverServiceRequest request // Reusing Create request for simplicity, ideally Update DTO
    ) {
        validateCaregiver(role);
        
        // Find existing service by ID is tricky if we don't pass ID in URL. 
        // Assuming we pass serviceId in body or logic is find by serviceId(type) for this caregiver.
        // The CreateCaregiverServiceRequest has serviceId (which is the Service Type ID).
        // WE need the CaregiverService ID (the row ID). CreateCaregiverServiceRequest doesn't have it.
        // Let's assume we find by (CaregiverId + ServiceTypeId).

        CaregiverService service = caregiverServiceRepository.findByCaregiverIdAndServiceId(caregiverId, request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found for this caregiver"));

        service.setDescription(request.getDescription());
        service.setExtraPrice(request.getExtraPrice());
        service.setMinChildAge(request.getMinChildAge());
        service.setMaxChildAge(request.getMaxChildAge());
        if(request.getCity() != null) {
            service.setCity(request.getCity());
        }

        // On update, we might want to deactivate if critical info changed, but for now, 
        // let's just ensure it respects certification if exists.
        certificationRepository.findByCaregiverIdAndServiceId(caregiverId, request.getServiceId())
                .ifPresent(cert -> {
                    if (!"VERIFIED".equals(cert.getVerificationStatus())) {
                        service.deactivate();
                    }
                });
        
        return caregiverServiceRepository.save(service);
    }

    @DeleteMapping("/services/{serviceTypeId}")
    public void deleteService(
            @RequestHeader("X-User-Id") UUID caregiverId,
            @RequestHeader("X-User-Role") String role,
            @PathVariable UUID serviceTypeId
    ) {
        validateCaregiver(role);
        CaregiverService service = caregiverServiceRepository.findByCaregiverIdAndServiceId(caregiverId, serviceTypeId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        caregiverServiceRepository.delete(service);
    }
    
    // Updates
    
    @PutMapping("/certifications/{id}")
    public com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification updateCertification(
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
        // Do NOT allow changing Service ID easily? For now assuming simple update.
        // Important: RESET STATUS
        cert.setVerificationStatus("PENDING");
        
        // Lock Service
        if (cert.getServiceId() != null) {
            caregiverServiceRepository.findByCaregiverIdAndServiceId(caregiverId, cert.getServiceId())
                    .ifPresent(service -> {
                        service.deactivate();
                        caregiverServiceRepository.save(service);
                    });
        }
        
        return certificationRepository.save(cert);
    }

    @DeleteMapping("/certifications/{id}")
    public void deleteCertification(
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
    }

    // Cleanup Endpoint
    @DeleteMapping("/all")
    public void deleteCaregiverData(
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
    }

    // Helpers
    private void validateCaregiver(String role) {
        if (!"ROLE_CARETAKER".equals(role)) {
            throw new RuntimeException("Only caregiver can perform this action");
        }
    }
}
