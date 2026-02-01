package com.carecircle.user_profile_service.admin.repository;

import com.carecircle.user_profile_service.admin.model.ProfileVerificationAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ProfileVerificationAuditRepository extends JpaRepository<ProfileVerificationAudit, UUID> {
}
