package com.carecircle.user_profile_service.child.repository;

import com.carecircle.user_profile_service.child.model.Child;
import com.carecircle.user_profile_service.parent.model.ParentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing Child persistence.
 *
 * All queries are scoped to the owning parent to enforce data isolation.
 */
public interface ChildRepository extends JpaRepository<Child, UUID> {

    /**
     * Fetch all children belonging to a specific parent.
     *
     * @param parent owning parent profile
     * @return list of children
     */
    List<Child> findAllByParent(ParentProfile parent);

    /**
     * Fetch a child by id, scoped to a parent.
     * Used to enforce ownership checks.
     *
     * @param id child id
     * @param parent owning parent profile
     * @return child if found and owned by parent
     */
    Optional<Child> findByIdAndParent(UUID id, ParentProfile parent);
    
    /*
     * Delete the children only using the parent
     */
    void deleteByIdAndParent(UUID id, ParentProfile parent);
    
    /**
     * Count children belonging to a specific parent.
     *
     * @param parent owning parent profile
     * @return number of children
     */
    long countByParent(ParentProfile parent);
    
}
