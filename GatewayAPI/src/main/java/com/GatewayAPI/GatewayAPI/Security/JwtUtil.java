package com.GatewayAPI.GatewayAPI.Security;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import java.nio.charset.StandardCharsets;
import java.security.Key;

/*
 * Utility class for parsing and validating JWT tokens. 
 * No request filtering logic is handled here. 
 */

@Component
public class JwtUtil {

	private final JwtProperties jwtProperties; 
	
	private Key signingKey;
	
	public JwtUtil(JwtProperties jwtProperties) {
		
		this.jwtProperties = jwtProperties;
	}
	
	@PostConstruct
	public void init() {
        this.signingKey = Keys.hmacShaKeyFor(
            jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)
        );
    }
	
	/*
	 * Extracts claims from a JWT token. 
	 * 
	 * @param token JWT token string 
	 * 
	 * @Param parsed JWT claims
	 * 
	 */
	
	public Claims extractClaims(String token) {
		
		return Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.build()
				.parseClaimsJws(token)
				.getBody(); 
	}
}


