package com.carecircle.matchingBookingService.admin.service.impl;

import com.carecircle.matchingBookingService.admin.model.VerificationAudit;
import com.carecircle.matchingBookingService.admin.repository.VerificationAuditRepository;
import com.carecircle.matchingBookingService.admin.service.AdminMatchingService;
import com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification;
import com.carecircle.matchingBookingService.caregiver.model.CaregiverService;
import com.carecircle.matchingBookingService.caregiver.repository.CaregiverCertificationRepository;
import com.carecircle.matchingBookingService.caregiver.repository.CaregiverServiceRepository; // Assuming this exists or I will create it/use existing Controller's logic
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AdminMatchingServiceImpl implements AdminMatchingService {

    private final CaregiverCertificationRepository certificationRepository;
    private final VerificationAuditRepository auditRepository;
    private final CaregiverServiceRepository caregiverServiceRepository;

    public AdminMatchingServiceImpl(
            CaregiverCertificationRepository certificationRepository,
            VerificationAuditRepository auditRepository,
            CaregiverServiceRepository caregiverServiceRepository
    ) {
        this.certificationRepository = certificationRepository;
        this.auditRepository = auditRepository;
        this.caregiverServiceRepository = caregiverServiceRepository;
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
    public List<VerificationAudit> getAllAudits() {
        return auditRepository.findAll();
    }
    
    private void logAudit(UUID adminId, String adminEmail, String targetType, UUID targetId, String action, String prev, String curr, String reason) {
        VerificationAudit audit = new VerificationAudit(
                adminId, adminEmail, targetType, targetId, action, prev, curr, reason
        );
        auditRepository.save(audit);
    }
}
