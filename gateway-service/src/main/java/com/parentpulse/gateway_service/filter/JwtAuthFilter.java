//package com.parentpulse.gateway_service.filter;
//
//import com.parentpulse.gateway_service.security.JwtUtil;
//
//import io.jsonwebtoken.Claims;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import reactor.core.publisher.Mono;
//
//@Component
//public class JwtAuthFilter implements GlobalFilter {
//
//	private final JwtUtil jwtUtil;
//
//	public JwtAuthFilter(JwtUtil jwtUtil) {
//		this.jwtUtil = jwtUtil;
//	}
//
//	@Override
//	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//
//	    String path = exchange.getRequest().getURI().getPath();
//
//	    // ===== PUBLIC ENDPOINTS (NO JWT, NO ROLE CHECK) =====
//	    if (
//	    	    path.startsWith("/auth/") ||
//	    	    path.startsWith("/actuator/")
//	    	) {
//	    	    return chain.filter(exchange);
//	    	}
//
//	    // ===== JWT REQUIRED =====
//	    String authHeader = exchange.getRequest()
//	            .getHeaders()
//	            .getFirst(HttpHeaders.AUTHORIZATION);
//
//	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//	        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);	
//	        return exchange.getResponse().setComplete();
//	    }
//
//	    String token = authHeader.substring(7);
//
//	    Claims claims;
//	    try {
//	        claims = jwtUtil.validateAndExtractClaims(token);
//	    } catch (Exception e) {
//	    	e.printStackTrace();
//	        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//	        return exchange.getResponse().setComplete();
//	    }
//
//	    String role = claims.get("role", String.class);
//	    
//	    String userId = claims.get("userId", String.class);
//
//	    // ===== ROLE-BASED AUTHORIZATION =====
//	    if (!isAuthorized(path, role)) {
//	        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//	        return exchange.getResponse().setComplete();
//	    }
//
//	    // ===== FORWARD TRUSTED HEADERS =====
//	    exchange = exchange.mutate()
//	        .request(builder -> builder
//	            .headers(headers -> {
//	                headers.remove("X-User-Email");
//	                headers.remove("X-User-Role");
//	                headers.remove("X-Request-Source");
//	            })
//	            .header("X-User-Email", claims.getSubject())
//	            .header("X-User-Role", role)
//	            .header("X-User-Id", userId)
//	            .header("X-Request-Source", "GATEWAY")
//	        )
//	        .build();
//
//
//	    return chain.filter(exchange);
//	}
//	
//		private boolean isAuthorized(String path, String role) {
//	
//		    if (path.startsWith("/admin/")) {
//		        return "ROLE_ADMIN".equals(role);
//		    }
//	
//		    if (path.startsWith("/caregiver/")) {
//		    	
//		        return "ROLE_CARETAKER".equals(role);
//		    }
//	
//		    if (path.startsWith("/parents/")) {
//		        return "ROLE_PARENT".equals(role);
//		    }
//	
//		    return true;
//		}
//	
//	}

package com.parentpulse.gateway_service.filter;

import com.parentpulse.gateway_service.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter {

    private final JwtUtil jwtUtil;

    // Gateway-level service prefixes (ADD MORE HERE LATER)
    private static final List<String> SERVICE_PREFIXES = List.of(
            "/matching-booking",
            "/user-profile",
            "/communication"
    );

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // ===== PUBLIC ENDPOINTS =====
        if (
                path.startsWith("/auth/") ||
                path.startsWith("/actuator/")
        ) {
            return chain.filter(exchange);
        }

        // ===== JWT REQUIRED =====
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        Claims claims;
        try {
            claims = jwtUtil.validateAndExtractClaims(token);
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String role = claims.get("role", String.class);
        String userId = claims.get("userId", String.class);

        // ===== NORMALIZE PATH (CRITICAL FIX) =====
        String normalizedPath = normalizePath(path);

        // ===== ROLE-BASED AUTHORIZATION =====
        if (!isAuthorized(normalizedPath, role)) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        // ===== FORWARD TRUSTED HEADERS =====
        exchange = exchange.mutate()
                .request(builder -> builder
                        .headers(headers -> {
                            headers.remove("X-User-Email");
                            headers.remove("X-User-Role");
                            headers.remove("X-Request-Source");
                        })
                        .header("X-User-Email", claims.getSubject())
                        .header("X-User-Role", role)
                        .header("X-User-Id", userId)
                        .header("X-Request-Source", "GATEWAY")
                )
                .build();

        return chain.filter(exchange);
    }

    // ===== PATH NORMALIZATION =====
    private String normalizePath(String path) {
        for (String prefix : SERVICE_PREFIXES) {
            if (path.startsWith(prefix + "/")) {
                return path.substring(prefix.length());
            }
        }
        return path;
    }

    // ===== AUTHORIZATION RULES =====
    private boolean isAuthorized(String path, String role) {

        if (path.startsWith("/admin/")) {
            return "ROLE_ADMIN".equals(role);
        }

        if (path.startsWith("/caregiver/")) {
            return "ROLE_CARETAKER".equals(role);
        }

        if (path.startsWith("/parents/")) {
            return "ROLE_PARENT".equals(role);
        }

        // Any authenticated user
        return true;
    }
}

