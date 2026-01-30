package com.carecircle.user_profile_service.common.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.core.ParameterizedTypeReference;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class CityIntegrationService {

    private final RestClient restClient;
    private static final String MATCHING_SERVICE_URL = "http://localhost:8085";

    public CityIntegrationService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(MATCHING_SERVICE_URL).build();
    }

    public record CityDto(UUID id, String name, String state, String country, Boolean active) {}

    public Optional<CityDto> getCityByName(String cityName) {
        try {
            List<CityDto> cities = restClient.get()
                    .uri("/cities")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<CityDto>>() {});

            if (cities == null) return Optional.empty();

            return cities.stream()
                    .filter(city -> city.name().equalsIgnoreCase(cityName) && city.active())
                    .findFirst();

        } catch (Exception e) {
            // Log error in real app
            System.err.println("Error fetching cities: " + e.getMessage());
            return Optional.empty();
        }
    }
}
