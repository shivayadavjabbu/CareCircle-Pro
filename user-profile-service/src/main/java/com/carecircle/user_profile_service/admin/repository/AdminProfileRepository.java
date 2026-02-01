package com.carecircle.user_profile_service.admin.repository;

import com.carecircle.user_profile_service.admin.model.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;


/**
 * Repository for AdminProfile persistence.
 */
public interface AdminProfileRepository extends JpaRepository<AdminProfile, UUID> {

    /**
     * Fetch admin profile by authenticated user email.
     */
    Optional<AdminProfile> findByUserEmail(String userEmail);
    Optional<AdminProfile> findByUserId(UUID userId);
    
    List<AdminProfile> findByUserIdIn(List<UUID> userIds);
}

