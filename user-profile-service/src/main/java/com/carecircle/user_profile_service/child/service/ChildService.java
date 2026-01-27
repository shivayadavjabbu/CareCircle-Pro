package com.carecircle.user_profile_service.child.service;

import com.carecircle.user_profile_service.child.exception.ChildNotFoundException;
import com.carecircle.user_profile_service.child.model.Child;
import com.carecircle.user_profile_service.child.repository.ChildRepository;
import com.carecircle.user_profile_service.parent.model.ParentProfile;
import com.carecircle.user_profile_service.parent.service.ParentProfileService;
import com.carecircle.user_profile_service.parent.service.impl.ParentProfileServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.UUID;

/**
 * Service responsible for child domain operations.
 *
 * Enforces ownership and parent-child relationship rules.
 */
@Service
public class ChildService {

    private final ChildRepository childRepository;
    private final ParentProfileService parentProfileService;

    public ChildService(
            ChildRepository childRepository,
            ParentProfileServiceImpl parentProfileService
    ) {
        this.childRepository = childRepository;
        this.parentProfileService = parentProfileService;
    }

    /**
     * Creates a new child for the authenticated parent.
     *
     * @param userEmail authenticated parent's email
     * @param name child name
     * @param age child age
     * @param gender optional gender
     * @param specialNeeds optional special needs
     * @return persisted Child
     */
    @Transactional
    public Child createChild(
            UUID userId,
            String name,
            Integer age,
            String gender,
            String specialNeeds
    ) {
        ParentProfile parent =
                parentProfileService.getProfileByUserId(userId);

        Child child = new Child(
                parent,
                name,
                age,
                gender,
                specialNeeds
        );

        return childRepository.save(child);
    }

    /**
     * Fetches all children belonging to the authenticated parent.
     *
     * @param userEmail authenticated parent's email
     * @return list of children
     */
    @Transactional(readOnly = true)
    public List<Child> getChildrenForParent(UUID userId) {
        ParentProfile parent =
                parentProfileService.getProfileByUserId(userId);

        return childRepository.findAllByParent(parent);
    }

    /**
     * Updates child details if the child belongs to the authenticated parent.
     *
     * @param userEmail authenticated parent's email
     * @param childId child id
     * @param name updated name
     * @param age updated age
     * @param gender updated gender
     * @param specialNeeds updated special needs
     * @return updated Child
     */
    @Transactional
    public Child updateChild(
            UUID userID,
            UUID childId,
            String name,
            Integer age,
            String gender,
            String specialNeeds
    ) {
        ParentProfile parent =
                parentProfileService.getProfileByUserId(userID);

        Child child = childRepository
                .findByIdAndParent(childId, parent)
                .orElseThrow(() -> new ChildNotFoundException(String.valueOf(childId)));

        child.updateDetails(name, age, gender, specialNeeds);

        return child;
    }
    
    
    /**
     * Deletes a child if it belongs to the authenticated parent.
     *
     * @param userEmail authenticated parent's email
     * @param childId child id
     */
    @Transactional
    public void deleteChild(UUID userId, UUID childId) {
        ParentProfile parent =
                parentProfileService.getProfileByUserId(userId);

        boolean exists = childRepository
                .findByIdAndParent(childId, parent)
                .isPresent();

        if (!exists) {
            throw new ChildNotFoundException(String.valueOf(childId));
        }

        childRepository.deleteByIdAndParent(childId, parent);
    }

    /**
     * Fetches a child by id if it belongs to the authenticated parent.
     *
     * @param userEmail authenticated parent's email
     * @param childId child id
     * @return child
     */
    @Transactional(readOnly = true)
    public Child getChildById(UUID userId, UUID childId) {
        ParentProfile parent =
                parentProfileService.getProfileByUserId(userId);

        return childRepository
                .findByIdAndParent(childId, parent)
                .orElseThrow(() -> new ChildNotFoundException(String.valueOf(childId)));
    }

}

