package com.carecircle.matchingBookingService.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.carecircle.matchingBookingService.city.model.City;
import com.carecircle.matchingBookingService.city.repository.CityRepository;
import com.carecircle.matchingBookingService.service.model.ServiceEntity;
import com.carecircle.matchingBookingService.service.repository.ServiceRepository;

/**
 * Initializes default cities and services on application startup.
 * Only creates data if not already present in the database.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final CityRepository cityRepository;
    private final ServiceRepository serviceRepository;

    public DataLoader(CityRepository cityRepository, ServiceRepository serviceRepository) {
        this.cityRepository = cityRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public void run(String... args) {
        initializeCities();
        initializeServices();
    }

    private void initializeCities() {
        logger.info("Checking if cities exist...");

        if (cityRepository.count() > 0) {
            logger.info("Cities already exist (count: {}). Skipping initialization.", cityRepository.count());
            return;
        }

        logger.info("Initializing default cities...");

        List<City> cities = List.of(
            new City("Mumbai", "Maharashtra", "India"),
            new City("Delhi", "Delhi", "India"),
            new City("Bangalore", "Karnataka", "India"),
            new City("Hyderabad", "Telangana", "India"),
            new City("Chennai", "Tamil Nadu", "India"),
            new City("Kolkata", "West Bengal", "India"),
            new City("Pune", "Maharashtra", "India"),
            new City("Ahmedabad", "Gujarat", "India"),
            new City("Jaipur", "Rajasthan", "India"),
            new City("Lucknow", "Uttar Pradesh", "India")
        );

        cityRepository.saveAll(cities);
        logger.info("Initialized {} cities successfully.", cities.size());
    }

    private void initializeServices() {
        logger.info("Checking if services exist...");

        if (serviceRepository.count() > 0) {
            logger.info("Services already exist (count: {}). Skipping initialization.", serviceRepository.count());
            return;
        }

        logger.info("Initializing default services...");

        List<ServiceEntity> services = List.of(
            new ServiceEntity("INFANT_CARE", "Infant Care (0-1 years)", "Childcare", 600.0),
            new ServiceEntity("TODDLER_CARE", "Toddler Care (1-3 years)", "Childcare", 550.0),
            new ServiceEntity("PRESCHOOL_CARE", "Preschool Care (3-5 years)", "Childcare", 500.0),
            new ServiceEntity("SCHOOL_AGE_CARE", "School Age Care (5-12 years)", "Childcare", 450.0),
            new ServiceEntity("TUTORING", "Academic Tutoring", "Education", 400.0),
            new ServiceEntity("HOMEWORK_HELP", "Homework Assistance", "Education", 350.0),
            new ServiceEntity("PHYSICAL_ACTIVITY", "Physical Exercise & Sports", "Health", 400.0),
            new ServiceEntity("SPECIAL_NEEDS", "Special Needs Care", "Specialized", 800.0),
            new ServiceEntity("OVERNIGHT_CARE", "Overnight Care", "Specialized", 1000.0),
            new ServiceEntity("HEALTH_MONITORING", "Health Monitoring & First Aid", "Health", 500.0)
        );

        serviceRepository.saveAll(services);
        logger.info("Initialized {} services successfully.", services.size());
    }
}
