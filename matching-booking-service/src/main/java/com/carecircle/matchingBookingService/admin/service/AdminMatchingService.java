package com.carecircle.matchingBookingService.admin.service;

import com.carecircle.matchingBookingService.admin.dto.CertificationVerificationAuditResponse;
import com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification;
import com.carecircle.matchingBookingService.common.dto.PagedResponse;

import java.util.List;
import java.util.UUID;

public interface AdminMatchingService {

    List<CaregiverCertification> getPendingCertifications();

    PagedResponse<CaregiverCertification> getPagedCertifications(List<String> statuses, int page, int size);

    void verifyCertification(UUID adminId, String adminEmail, UUID certificationId, String reason);

    void rejectCertification(UUID adminId, String adminEmail, UUID certificationId, String reason);

    void disableCertification(UUID adminId, String adminEmail, UUID certificationId, String reason);

    List<CertificationVerificationAuditResponse> getCertificationAudits();
}
