package com.carecircle.user_profile_service.parent.service.impl;


import com.carecircle.user_profile_service.parent.exception.ParentProfileAlreadyExistsException;
import com.carecircle.user_profile_service.parent.exception.ParentProfileNotFoundException;
import com.carecircle.user_profile_service.parent.model.ParentProfile;
import com.carecircle.user_profile_service.parent.repository.ParentProfileRepository;
import com.carecircle.user_profile_service.parent.service.ParentProfileService;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for handling parent profile domain logic.
 *
 * This layer coordinates persistence and enforces business rules
 * related to parent profiles.
 */
@Service
public class ParentProfileServiceImpl implements ParentProfileService{

    private final ParentProfileRepository parentProfileRepository;

    public ParentProfileServiceImpl(ParentProfileRepository parentProfileRepository) {
        this.parentProfileRepository = parentProfileRepository;
    }

    /**
     * Creates a new parent profile for the authenticated user.
     *
     * @param userEmail email injected by API Gateway
     * @param fullName parent's full name
     * @param phoneNumber contact phone number
     * @param address residential address
     * @return persisted ParentProfile
     * @throws IllegalStateException if profile already exists
     */
    @Transactional
    public ParentProfile createProfile(
    		UUID userId,
            String userEmail,
            String fullName,
            String phoneNumber,
            String address
    ) {
        boolean exists = parentProfileRepository.findByUserEmail(userEmail).isPresent();
        if (exists) {
        	 throw new ParentProfileAlreadyExistsException(userEmail);
        }

        ParentProfile profile =
                new ParentProfile( userId, userEmail, fullName, phoneNumber, address);

        return parentProfileRepository.save(profile);
    }

    /**
     * Fetches the parent profile for the authenticated user.
     *
     * @param userEmail email injected by API Gateway
     * @return ParentProfile
     * @throws IllegalStateException if profile does not exist
     */
    @Transactional(readOnly = true)
    public ParentProfile getProfileByUserEmail(String emailId) {
        return parentProfileRepository
                .findByUserEmail(emailId)
                .orElseThrow(() ->
                        new ParentProfileNotFoundException(emailId)
                );
    }


	@Override
	public ParentProfile getProfileByUserId(UUID userId) {
		 return parentProfileRepository
	                .findByUserID(userId)
	                .orElseThrow(() ->
	                        new ParentProfileNotFoundException(String.valueOf(userId))
	                );
	}
}
