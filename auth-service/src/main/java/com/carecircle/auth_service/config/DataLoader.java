package com.carecircle.auth_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.carecircle.auth_service.loginRegister.model.Role;
import com.carecircle.auth_service.loginRegister.model.User;
import com.carecircle.auth_service.loginRegister.repository.UserRepository;

/**
 * Initializes default admin user on application startup.
 * Only creates user if not already present in the database.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private static final String ADMIN_EMAIL = "roleadmin@123.com";
    private static final String ADMIN_PASSWORD = "12345";
    private static final Role ADMIN_ROLE = Role.ROLE_ADMIN;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        initializeAdminUser();
    }

    private void initializeAdminUser() {
        logger.info("Checking if admin user exists...");

        boolean adminExists = userRepository.findByEmailAndRole(ADMIN_EMAIL, ADMIN_ROLE).isPresent();

        if (adminExists) {
            logger.info("Admin user already exists. Skipping creation.");
            return;
        }

        logger.info("Creating default admin user: {}", ADMIN_EMAIL);

        User admin = new User();
        admin.setEmail(ADMIN_EMAIL);
        admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
        admin.setRole(ADMIN_ROLE);
        admin.setEnabled(true);

        userRepository.save(admin);

        logger.info("Default admin user created successfully: {}", ADMIN_EMAIL);
    }
}
