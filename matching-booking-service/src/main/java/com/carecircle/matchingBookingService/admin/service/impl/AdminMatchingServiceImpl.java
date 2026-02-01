package com.carecircle.matchingBookingService.admin.service.impl;

import com.carecircle.matchingBookingService.admin.dto.CertificationVerificationAuditResponse;
import com.carecircle.matchingBookingService.admin.model.CertificationVerificationAudit;
import com.carecircle.matchingBookingService.admin.repository.CertificationVerificationAuditRepository;
import com.carecircle.matchingBookingService.admin.service.AdminMatchingService;
import com.carecircle.matchingBookingService.common.service.UserIntegrationService;
import com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification;
import com.carecircle.matchingBookingService.caregiver.model.CaregiverService;
import com.carecircle.matchingBookingService.caregiver.repository.CaregiverCertificationRepository;
import com.carecircle.matchingBookingService.caregiver.repository.CaregiverServiceRepository;
import com.carecircle.matchingBookingService.service.repository.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminMatchingServiceImpl implements AdminMatchingService {

    private final CaregiverCertificationRepository certificationRepository;
    private final CertificationVerificationAuditRepository auditRepository;
    private final CaregiverServiceRepository caregiverServiceRepository;
    private final ServiceRepository serviceRepository;
    private final UserIntegrationService userService;

    public AdminMatchingServiceImpl(
            CaregiverCertificationRepository certificationRepository,
            CertificationVerificationAuditRepository auditRepository,
            CaregiverServiceRepository caregiverServiceRepository,
            ServiceRepository serviceRepository,
            UserIntegrationService userService
    ) {
        this.certificationRepository = certificationRepository;
        this.auditRepository = auditRepository;
        this.caregiverServiceRepository = caregiverServiceRepository;
        this.serviceRepository = serviceRepository;
        this.userService = userService;
    }

    @Override
    public List<CaregiverCertification> getPendingCertifications() {
        return certificationRepository.findAll().stream()
                .filter(c -> "PENDING".equals(c.getVerificationStatus()))
                .toList();
    }

    @Override
    public void verifyCertification(UUID adminId, String adminEmail, UUID certificationId, String reason) {
        CaregiverCertification cert = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new RuntimeException("Certification not found"));

        if ("VERIFIED".equals(cert.getVerificationStatus())) {
            throw new RuntimeException("Already verified");
        }

        String previousStatus = cert.getVerificationStatus();
        cert.setVerificationStatus("VERIFIED");
        certificationRepository.save(cert);

        logAudit(adminId, adminEmail, "CERTIFICATION", certificationId, "VERIFY", previousStatus, "VERIFIED", reason);

        // Enable associated service if applicable
        if (cert.getServiceId() != null) {
            caregiverServiceRepository.findByCaregiverIdAndServiceId(cert.getCaregiverId(), cert.getServiceId())
                    .ifPresent(service -> {
                        // We can implicitly 'activate' the service or we allow the user to define 'active' 
                        // but logic checks certification. For now, let's just log or ensure it's not disabled by system.
                    });
        }
    }

    @Override
    public void rejectCertification(UUID adminId, String adminEmail, UUID certificationId, String reason) {
        CaregiverCertification cert = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new RuntimeException("Certification not found"));

        String previousStatus = cert.getVerificationStatus();
        cert.setVerificationStatus("REJECTED");
        certificationRepository.save(cert);

        logAudit(adminId, adminEmail, "CERTIFICATION", certificationId, "REJECT", previousStatus, "REJECTED", reason);

        // Disable associated service
        if (cert.getServiceId() != null) {
            caregiverServiceRepository.findByCaregiverIdAndServiceId(cert.getCaregiverId(), cert.getServiceId())
                    .ifPresent(service -> {
                        service.deactivate();
                        caregiverServiceRepository.save(service);
                        logAudit(adminId, adminEmail, "SERVICE", service.getId(), "DISABLE", "ACTIVE", "DISABLED", "Certification Rejected: " + reason);
                    });
        }
    }

    @Override
    public void disableCertification(UUID adminId, String adminEmail, UUID certificationId, String reason) {
        // Similar to reject, but maybe "SUSPENDED" status? For now treating as REJECTED flow or just logging.
        // Assuming Disable means Deactivate/Reject.
         rejectCertification(adminId, adminEmail, certificationId, reason);
    }

    @Override
    public List<CertificationVerificationAuditResponse> getCertificationAudits() {
        List<CertificationVerificationAudit> audits = auditRepository.findAll();
        if (audits.isEmpty()) return Collections.emptyList();

        // Collect all IDs needed for enrichment
        Set<UUID> adminIds = audits.stream().map(CertificationVerificationAudit::getAdminId).collect(Collectors.toSet());
        
        // Map to hold temp data for each audit
        Map<UUID, UUID> auditToCaregiverId = new HashMap<>();
        Map<UUID, String> auditToTargetName = new HashMap<>();

        for (CertificationVerificationAudit audit : audits) {
            if ("CERTIFICATION".equals(audit.getTargetType())) {
                certificationRepository.findById(audit.getTargetId()).ifPresent(c -> {
                    auditToCaregiverId.put(audit.getId(), c.getCaregiverId());
                    auditToTargetName.put(audit.getId(), c.getName());
                });
            } else if ("SERVICE".equals(audit.getTargetType())) {
                caregiverServiceRepository.findById(audit.getTargetId()).ifPresent(cs -> {
                    auditToCaregiverId.put(audit.getId(), cs.getCaregiverId());
                    serviceRepository.findById(cs.getServiceId()).ifPresent(s -> 
                        auditToTargetName.put(audit.getId(), s.getServiceName())
                    );
                });
            }
        }

        Set<UUID> caregiverIds = new HashSet<>(auditToCaregiverId.values());
        
        // Batch fetch all names
        List<UUID> allUserIds = new ArrayList<>(adminIds);
        allUserIds.addAll(caregiverIds);
        
        Map<UUID, UserIntegrationService.UserSummary> userMap = userService.getUsersInfo(allUserIds);

        return audits.stream().map(a -> {
            String adminName = userMap.containsKey(a.getAdminId()) ? userMap.get(a.getAdminId()).fullName() : "Unknown Admin";
            UUID caregiverId = auditToCaregiverId.get(a.getId());
            String caregiverName = (caregiverId != null && userMap.containsKey(caregiverId)) 
                    ? userMap.get(caregiverId).fullName() : "Unknown Caregiver";
            
            return new CertificationVerificationAuditResponse(
                a.getId(),
                a.getAdminId(),
                adminName,
                a.getTargetType(),
                a.getTargetId(),
                auditToTargetName.getOrDefault(a.getId(), "Unknown Target"),
                caregiverId,
                caregiverName,
                a.getAction(),
                a.getPreviousStatus(),
                a.getNewStatus(),
                a.getReason(),
                a.getCreatedAt()
            );
        }).sorted(Comparator.comparing(CertificationVerificationAuditResponse::getCreatedAt).reversed()).toList();
    }
    
    private void logAudit(UUID adminId, String adminEmail, String targetType, UUID targetId, String action, String prev, String curr, String reason) {
        CertificationVerificationAudit audit = new CertificationVerificationAudit(
                adminId, adminEmail, targetType, targetId, action, prev, curr, reason
        );
        auditRepository.save(audit);
    }
}
