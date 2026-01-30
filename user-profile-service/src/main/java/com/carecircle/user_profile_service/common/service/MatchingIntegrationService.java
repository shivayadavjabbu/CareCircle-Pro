package com.carecircle.user_profile_service.common.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.core.ParameterizedTypeReference;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.Collections;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class MatchingIntegrationService {

    private final RestClient restClient;
    private static final String MATCHING_SERVICE_URL = "http://localhost:8085";

    public MatchingIntegrationService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(MATCHING_SERVICE_URL).build();
    }

    public record CityDto(UUID id, String name, String state, String country, Boolean active) {}
    public record ServiceDto(UUID id, String code, String name, String category, Double basePrice, Boolean active) {}

    /**
     * Fetch all active cities from matching-booking-service.
     */
    public List<CityDto> getAllCities() {
        try {
            var requestSpec = restClient.get().uri("/cities");
            addAuthHeaders(requestSpec);

            List<CityDto> cities = requestSpec
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<CityDto>>() {});

            return cities != null ? cities : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error fetching all cities: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Optional<CityDto> getCityByName(String cityName) {
        try {
            var requestSpec = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/cities/lookup")
                            .queryParam("name", cityName)
                            .build());
            addAuthHeaders(requestSpec);

            CityDto city = requestSpec
                    .retrieve()
                    .body(CityDto.class);

            return Optional.ofNullable(city);
        } catch (Exception e) {
            System.err.println("Error looking up city: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Fetch all active services from matching-booking-service.
     */
    public List<ServiceDto> getAllServices() {
        try {
            var requestSpec = restClient.get().uri("/services");
            addAuthHeaders(requestSpec);

            List<ServiceDto> services = requestSpec
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ServiceDto>>() {});

            return services != null ? services : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error fetching all services: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Optional<ServiceDto> getServiceByCode(String code) {
        try {
            var requestSpec = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/services/lookup")
                            .queryParam("code", code)
                            .build());
            addAuthHeaders(requestSpec);

            ServiceDto service = requestSpec
                    .retrieve()
                    .body(ServiceDto.class);

            return Optional.ofNullable(service);
        } catch (Exception e) {
            System.err.println("Error looking up service by code: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<ServiceDto> getServiceByName(String name) {
        try {
            var requestSpec = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/services/lookup-by-name")
                            .queryParam("name", name)
                            .build());
            addAuthHeaders(requestSpec);

            ServiceDto service = requestSpec
                    .retrieve()
                    .body(ServiceDto.class);

            return Optional.ofNullable(service);
        } catch (Exception e) {
            System.err.println("Error looking up service by name: " + e.getMessage());
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
