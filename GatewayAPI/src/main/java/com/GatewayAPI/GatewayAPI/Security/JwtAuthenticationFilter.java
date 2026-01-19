package com.GatewayAPI.GatewayAPI.Security;

import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Global JWT authentication and role-based authorization filter.
 *
 * Responsibilities:
 * - Validate JWT token
 * - Extract user identity and role
 * - Enforce role-based access at gateway level
 * - Forward trusted user headers to downstream services
 */
@Component
public class JwtAuthenticationFilter implements GlobalFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // Public endpoints
        if (path.startsWith("/auth/")) {
            return chain.filter(exchange);
        }

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
            claims = jwtUtil.extractClaims(token);
        } catch (Exception ex) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String role = claims.get("role", String.class);

        // ---- ROLE BASED ACCESS CONTROL ----
        if (!isAuthorized(path, role)) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        // Forward trusted identity headers
        exchange = exchange.mutate()
                .request(builder -> builder
                        .header("X-User-Email", claims.getSubject())
                        .header("X-User-Role", role)
                )
                .build();

        return chain.filter(exchange);
    }

    /**
     * Simple role-to-path authorization rules.
     * This keeps gateway authorization coarse-grained.
     */
    private boolean isAuthorized(String path, String role) {

        if (path.startsWith("/admin/")) {
            return "ROLE_ADMIN".equals(role);
        }

        if (path.startsWith("/provider/")) {
            return "ROLE_PROVIDER".equals(role);
        }

        if (path.startsWith("/parent/")) {
            return "ROLE_PARENT".equals(role);
        }

        // Default: allow if authenticated
        return true;
    }
}
