package com.carecircle.matchingBookingService.city.repository;

import com.carecircle.matchingBookingService.city.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CityRepository extends JpaRepository<City, UUID> {

    List<City> findByActiveTrue();
}
