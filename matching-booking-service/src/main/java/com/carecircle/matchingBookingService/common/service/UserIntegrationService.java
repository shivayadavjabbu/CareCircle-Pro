package com.carecircle.matchingBookingService.common.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UserIntegrationService {

    private final RestClient restClient;
    private static final String USER_PROFILE_SERVICE_URL = "http://localhost:8083";

    public UserIntegrationService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(USER_PROFILE_SERVICE_URL).build();
    }

    public record UserSummary(UUID userId, String fullName, String userRole) {}

    public record UserBatchRequest(List<UUID> userIds) {}

    /**
     * Fetch multiple user summaries by their IDs.
     */
    public Map<UUID, UserSummary> getUsersInfo(List<UUID> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyMap();
        }

        try {
            var requestSpec = restClient.post()
                    .uri("/api/lookup/users")
                    .body(new UserBatchRequest(userIds));
            
            addAuthHeaders(requestSpec);

            List<UserSummary> summaries = requestSpec
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<UserSummary>>() {});

            if (summaries == null) return Collections.emptyMap();

            return summaries.stream()
                    .collect(Collectors.toMap(UserSummary::userId, s -> s, (s1, s2) -> s1));
        } catch (Exception e) {
            System.err.println("Error fetching users info: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    /**
     * Fetch a single user summary by ID.
     */
    public Optional<UserSummary> getUserInfo(UUID userId) {
        try {
            var requestSpec = restClient.get()
                    .uri("/api/lookup/users/" + userId);
            
            addAuthHeaders(requestSpec);

            UserSummary summary = requestSpec
                    .retrieve()
                    .body(UserSummary.class);

            return Optional.ofNullable(summary);
        } catch (Exception e) {
            System.err.println("Error fetching user info for " + userId + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    private void addAuthHeaders(RestClient.RequestHeadersSpec<?> requestSpec) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest currentRequest = attributes.getRequest();
            String auth = currentRequest.getHeader("Authorization");
            String userEmail = currentRequest.getHeader("X-User-Email");
            String userRole = currentRequest.getHeader("X-User-Role");
            String userId = currentRequest.getHeader("X-User-Id");

            if (auth != null) requestSpec.header("Authorization", auth);
            if (userEmail != null) requestSpec.header("X-User-Email", userEmail);
            if (userRole != null) requestSpec.header("X-User-Role", userRole);
            if (userId != null) requestSpec.header("X-User-Id", userId);
        }
    }
}
