package com.carecircle.matchingBookingService.admin.controller;

import com.carecircle.matchingBookingService.admin.dto.CertificationVerificationAuditResponse;
import com.carecircle.matchingBookingService.common.dto.PagedResponse;
import com.carecircle.matchingBookingService.admin.service.AdminMatchingService;
import com.carecircle.matchingBookingService.caregiver.model.CaregiverCertification;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/matching")
public class AdminMatchingController {

    private static final String USER_ROLE_HEADER = "X-User-Role";
    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    private static final String USER_ID = "X-User-Id";

    private final AdminMatchingService adminMatchingService;

    public AdminMatchingController(AdminMatchingService adminMatchingService) {
        this.adminMatchingService = adminMatchingService;
    }

    @GetMapping("/certifications/pending")
    public ResponseEntity<List<CaregiverCertification>> getPendingCertifications(HttpServletRequest request) {
        validateAdmin(request);
        return ResponseEntity.ok(adminMatchingService.getPendingCertifications());
    }

    @GetMapping("/certifications")
    public ResponseEntity<PagedResponse<CaregiverCertification>> getAllCertifications(
            @RequestParam(required = false) List<String> status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {
        validateAdmin(httpRequest);
        return ResponseEntity.ok(adminMatchingService.getPagedCertifications(status, page, size));
    }

    @GetMapping("/audits/certifications")
    public ResponseEntity<List<CertificationVerificationAuditResponse>> getCertificationAudits(
            HttpServletRequest request) {
        validateAdmin(request);
        return ResponseEntity.ok(adminMatchingService.getCertificationAudits());
    }

    @PostMapping("/certifications/{id}/verify")
    public ResponseEntity<Void> verifyCertification(
            @PathVariable UUID id,
            @RequestBody @Valid VerificationRequest request,
            HttpServletRequest httpRequest) {
        UUID adminId = validateAdmin(httpRequest);
        String adminEmail = getAdminEmail(httpRequest);
        adminMatchingService.verifyCertification(adminId, adminEmail, id, request.getReason());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/certifications/{id}/reject")
    public ResponseEntity<Void> rejectCertification(
            @PathVariable UUID id,
            @RequestBody @Valid VerificationRequest request,
            HttpServletRequest httpRequest) {
        UUID adminId = validateAdmin(httpRequest);
        String adminEmail = getAdminEmail(httpRequest);
        adminMatchingService.rejectCertification(adminId, adminEmail, id, request.getReason());
        return ResponseEntity.ok().build();
    }

    private UUID validateAdmin(HttpServletRequest request) {
        String role = request.getHeader(USER_ROLE_HEADER);
        if (!ADMIN_ROLE.equals(role)) {
            throw new RuntimeException("Access denied: admin role required");
        }
        String userId = request.getHeader(USER_ID);
        if (userId == null)
            throw new RuntimeException("Missing X-User-Id");
        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid X-User-Id");
        }
    }

    private String getAdminEmail(HttpServletRequest request) {
        return request.getHeader("X-User-Email");
    }

    // Inner DTO
    public static class VerificationRequest {
        @NotBlank
        private String reason;

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
