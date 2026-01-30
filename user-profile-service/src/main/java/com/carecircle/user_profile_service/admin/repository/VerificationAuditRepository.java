package com.carecircle.user_profile_service.admin.repository;

import com.carecircle.user_profile_service.admin.model.VerificationAudit;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for VerificationAudit persistence.
 *
 * This repository is append-only by design.
 */
public interface VerificationAuditRepository
        extends JpaRepository<VerificationAudit, UUID> {
}
