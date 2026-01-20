package com.parentpulse.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parentpulse.auth.model.User;

/*
 * Repository interface for User entity.
 * Acts as the persistence boundary for the Auth Service. 
 */
public interface UserRepository extends JpaRepository<User, Long>{

	/*
	 * find a user by email address. 
	 * @param email unique for email identifier
	 * @return optional user
	 */
	
	Optional<User> findByEmail(String email); 
	
	 /**
     * Check if a user exists with the given email.
     *
     * @param email unique email identifier
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);
       
}
