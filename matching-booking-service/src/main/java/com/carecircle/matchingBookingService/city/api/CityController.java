package com.carecircle.matchingBookingService.city.api;

import com.carecircle.matchingBookingService.city.api.dto.CreateCityRequest;
import com.carecircle.matchingBookingService.city.model.City;
import com.carecircle.matchingBookingService.city.repository.CityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CityController {

    private final CityRepository cityRepository;

    public CityController(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    // ADMIN ONLY
    @PostMapping("/admin/cities")
    public ResponseEntity<City> createCity(
            @RequestHeader("X-User-Role") String role,
            @RequestBody CreateCityRequest request
    ) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Only admin can create cities");
        }

        City city = new City(
                request.getName(),
                request.getState(),
                request.getCountry()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(cityRepository.save(city));
    }

    // PUBLIC
    @GetMapping("/cities")
    public ResponseEntity<List<City>> getActiveCities() {
        return ResponseEntity.ok(cityRepository.findByActiveTrue());
    }

    @GetMapping("/cities/lookup")
    public ResponseEntity<City> getCityByName(@RequestParam String name) {
        City city = cityRepository.findByNameIgnoreCaseAndActiveTrue(name)
                .orElseThrow(() -> new RuntimeException("City not found: " + name));
        return ResponseEntity.ok(city);
    }

    @GetMapping("/cities/{id}")
    public ResponseEntity<City> getCityById(@PathVariable UUID id) {
        City city = cityRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("City not found: " + id));
        return ResponseEntity.ok(city);
    }

    @GetMapping("/cities/id")
    public ResponseEntity<UUID> getCityIdByName(@RequestParam String name) {
        UUID cityId = cityRepository.findByNameIgnoreCaseAndActiveTrue(name)
                .map(City::getId)
                .orElseThrow(() -> new RuntimeException("City not found: " + name));
        return ResponseEntity.ok(cityId);
    }

    // ADMIN UPDATES & DELETES

    @PutMapping("/admin/cities/{id}")
    public ResponseEntity<City> updateCity(
            @PathVariable UUID id,
            @RequestHeader("X-User-Role") String role,
            @RequestBody com.carecircle.matchingBookingService.city.api.dto.UpdateCityRequest request
    ) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Only admin can update cities");
        }

        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found: " + id));

        city.setName(request.getName());
        city.setState(request.getState());
        city.setCountry(request.getCountry());

        return ResponseEntity.ok(cityRepository.save(city));
    }

    @DeleteMapping("/admin/cities/{id}")
    public ResponseEntity<Void> deleteCity(
            @PathVariable UUID id,
            @RequestHeader("X-User-Role") String role
    ) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Only admin can delete cities");
        }

        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found: " + id));

        city.deactivate();
        cityRepository.save(city);
        return ResponseEntity.noContent().build();
    }
}
