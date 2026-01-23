package com.carecircle.user_profile_service.parent.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.carecircle.user_profile_service.parent.model.ParentProfile;

/*
 *Repository for managing Parent Profile persistance
 *
 *This repository is resposible only for data access 
 */
public interface ParentProfileRepository extends JpaRepository<ParentProfile, Long> {

	/**
	 * Fetch a parent profile using the authenticated user's email.
	 *
	 * @param userEmail email injected by API Gateway
	 * @return parent profile if exists
	 */
	Optional<ParentProfile> findByUserEmail(String userEmail);

}
