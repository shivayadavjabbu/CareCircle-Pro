package com.carecircle.auth_service.loginRegister.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.carecircle.auth_service.loginRegister.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * JWT utility for AUTH SERVICE.
 *
 * Responsibilities:
 * - Generate JWT tokens after successful authentication
 *
 * NOTE:
 * - Token validation is handled by API Gateway
 */
@Component
public class JwtUtil {

    private final Key signingKey;
    
    private final long expiration;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.signingKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
        this.expiration = expiration;
    }

    /**
     * Generate JWT token for authenticated user
     */
    public String generateToken(User user) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(user.getEmail()) 
                .claim("emailId", user.getEmail())
                .claim("userId", user.getId().toString()) //Adding uuid
                .claim("role", user.getRole().name())     // authorization
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
