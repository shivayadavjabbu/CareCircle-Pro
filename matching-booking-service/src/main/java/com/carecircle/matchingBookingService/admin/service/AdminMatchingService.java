package com.carecircle.matchingBookingService.admin.service;

import com.carecircle.matchingBookingService.admin.dto.CertificationVerificationAuditResponse;
import com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification;

import java.util.List;
import java.util.UUID;

public interface AdminMatchingService {
    
    List<CaregiverCertification> getPendingCertifications();
    
    void verifyCertification(UUID adminId, String adminEmail, UUID certificationId, String reason);
    
    void rejectCertification(UUID adminId, String adminEmail, UUID certificationId, String reason);
    
    void disableCertification(UUID adminId, String adminEmail, UUID certificationId, String reason);

    List<CertificationVerificationAuditResponse> getCertificationAudits();
}
