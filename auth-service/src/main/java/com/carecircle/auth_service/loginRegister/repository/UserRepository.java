package com.carecircle.auth_service.loginRegister.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carecircle.auth_service.loginRegister.model.Role;
import com.carecircle.auth_service.loginRegister.model.User;


public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmailAndRole(String email, Role role);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}