package com.carecircle.matchingBookingService.admin.repository;

import com.carecircle.matchingBookingService.admin.model.CertificationVerificationAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CertificationVerificationAuditRepository extends JpaRepository<CertificationVerificationAudit, UUID> {
}
