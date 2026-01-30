package com.carecircle.auth_service.emailService.repository;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carecircle.auth_service.emailService.model.EmailOtp;
import com.carecircle.auth_service.loginRegister.model.Role;

public interface EmailOtpRepository extends JpaRepository<EmailOtp, UUID> {

    Optional<EmailOtp> findByEmailAndRole(String email, Role role);

    void deleteByEmailAndRole(String email, Role role);
}
