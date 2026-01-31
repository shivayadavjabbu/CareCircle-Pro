package com.carecircle.matchingBookingService.city.api;

import com.carecircle.matchingBookingService.city.api.dto.CreateCityRequest;
import com.carecircle.matchingBookingService.city.model.City;
import com.carecircle.matchingBookingService.city.repository.CityRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CityController {

    private final CityRepository cityRepository;

    public CityController(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    // ADMIN ONLY
    @PostMapping("/admin/cities")
    public City createCity(
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

        return cityRepository.save(city);
    }

    // PUBLIC
    @GetMapping("/cities")
    public List<City> getActiveCities() {
        return cityRepository.findByActiveTrue();
    }

    @GetMapping("/cities/lookup")
    public City getCityByName(@RequestParam String name) {
        return cityRepository.findByNameIgnoreCaseAndActiveTrue(name)
                .orElseThrow(() -> new RuntimeException("City not found: " + name));
    }

    @GetMapping("/cities/{id}")
    public City getCityById(@PathVariable java.util.UUID id) {
        return cityRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("City not found: " + id));
    }

    @GetMapping("/cities/id")
    public java.util.UUID getCityIdByName(@RequestParam String name) {
        return cityRepository.findByNameIgnoreCaseAndActiveTrue(name)
                .map(City::getId)
                .orElseThrow(() -> new RuntimeException("City not found: " + name));
    }

    // ADMIN UPDATES & DELETES

    @PutMapping("/admin/cities/{id}")
    public City updateCity(
            @PathVariable java.util.UUID id,
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

        return cityRepository.save(city);
    }

    @DeleteMapping("/admin/cities/{id}")
    public void deleteCity(
            @PathVariable java.util.UUID id,
            @RequestHeader("X-User-Role") String role
    ) {
        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Only admin can delete cities");
        }

        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found: " + id));

        city.deactivate();
        cityRepository.save(city);
    }
}
