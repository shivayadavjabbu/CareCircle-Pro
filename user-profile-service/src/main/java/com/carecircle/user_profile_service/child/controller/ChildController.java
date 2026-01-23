package com.carecircle.user_profile_service.child.controller;

import com.carecircle.user_profile_service.child.dto.ChildResponse;
import com.carecircle.user_profile_service.child.dto.CreateChildRequest;
import com.carecircle.user_profile_service.child.dto.UpdateChildRequest;
import com.carecircle.user_profile_service.child.model.Child;
import com.carecircle.user_profile_service.child.service.ChildService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing children under a parent profile.
 *
 * All endpoints require the authenticated user to have PARENT role.
 */
@RestController
@RequestMapping("/parents/children")
public class ChildController {

    private static final String USER_EMAIL_HEADER = "X-User-Email";
    private static final String USER_ROLE_HEADER = "X-User-Role";
    private static final String PARENT_ROLE = "PARENT";

    private final ChildService childService;

    public ChildController(ChildService childService) {
        this.childService = childService;
    }

    /**
     * Creates a new child for the authenticated parent.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChildResponse createChild(
    		@Valid @RequestBody CreateChildRequest request,
            HttpServletRequest httpRequest
    ) {
        String userEmail = extractUserEmail(httpRequest);
        validateParentRole(httpRequest);

        Child child = childService.createChild(
                userEmail,
                request.getName(),
                request.getAge(),
                request.getGender(),
                request.getSpecialNeeds()
        );

        return mapToResponse(child);
    }

    /**
     * Returns all children belonging to the authenticated parent.
     */
    @GetMapping
    public List<ChildResponse> getMyChildren(HttpServletRequest httpRequest) {
        String userEmail = extractUserEmail(httpRequest);
        validateParentRole(httpRequest);

        return childService.getChildrenForParent(userEmail)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Updates child details if the child belongs to the authenticated parent.
     */
    @PutMapping("/{childId}")
    public ChildResponse updateChild(
            @PathVariable Long childId,
            @Valid @RequestBody UpdateChildRequest request,
            HttpServletRequest httpRequest
    ) {
        String userEmail = extractUserEmail(httpRequest);
        validateParentRole(httpRequest);

        Child updatedChild = childService.updateChild(
                userEmail,
                childId,
                request.getName(),
                request.getAge(),
                request.getGender(),
                request.getSpecialNeeds()
        );

        return mapToResponse(updatedChild);
    }
    
    /**
     * Deletes a child owned by the authenticated parent.
     */
    @DeleteMapping("/{childId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChild(
            @PathVariable Long childId,
            HttpServletRequest httpRequest
    ) {
        String userEmail = extractUserEmail(httpRequest);
        validateParentRole(httpRequest);

        childService.deleteChild(userEmail, childId);
    }
    
    
    /**
     * Returns details of a specific child owned by the authenticated parent.
     */
    @GetMapping("/{childId}")
    public ChildResponse getChildById(
            @PathVariable Long childId,
            HttpServletRequest httpRequest
    ) {
        String userEmail = extractUserEmail(httpRequest);
        validateParentRole(httpRequest);

        Child child = childService.getChildById(userEmail, childId);

        return mapToResponse(child);
    }

    

    // =========================
    // Private helper methods
    // =========================

    private String extractUserEmail(HttpServletRequest request) {
        String email = request.getHeader(USER_EMAIL_HEADER);
        if (email == null || email.isBlank()) {
            throw new RuntimeException("Missing X-User-Email header");
        }
        return email;
    }

    private void validateParentRole(HttpServletRequest request) {
        String role = request.getHeader(USER_ROLE_HEADER);
        if (!PARENT_ROLE.equals(role)) {
            throw new RuntimeException("Access denied: user is not a parent");
        }
    }

    private ChildResponse mapToResponse(Child child) {
        return new ChildResponse(
                child.getId(),
                child.getName(),
                child.getAge(),
                child.getGender(),
                child.getSpecialNeeds(),
                child.getCreatedAt()
        );
    }
}
