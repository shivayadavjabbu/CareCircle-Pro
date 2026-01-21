package com.GatewayAPI.GatewayAPI.Security;

import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

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

        // --------------------------------------------------
        // 1. Resolve ORIGINAL request path (before rewriting)
        // --------------------------------------------------
        String originalPath = resolveOriginalPath(exchange);

        // Public endpoints (no authentication required)
        if (originalPath.startsWith("/auth/")) {
            return chain.filter(exchange);
        }

        // --------------------------------------------------
        // 2. Extract Authorization header
        // --------------------------------------------------
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        // --------------------------------------------------
        // 3. Validate JWT token
        // --------------------------------------------------
        Claims claims;
        try {
            claims = jwtUtil.extractClaims(token);
        } catch (Exception ex) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String role = claims.get("role", String.class);

        // --------------------------------------------------
        // 4. Role-Based Access Control (RBAC)
        // --------------------------------------------------
        if (!isAuthorized(originalPath, role)) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        // --------------------------------------------------
        // 5. Forward trusted user context to downstream services
        // --------------------------------------------------
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(builder -> builder
                        .header("X-User-Email", claims.getSubject())
                        .header("X-User-Role", role)
                )
                .build();

        return chain.filter(mutatedExchange);
    }

    /**
     * Resolve the original request path before any gateway rewriting.
     */
    private String resolveOriginalPath(ServerWebExchange exchange) {

        List<URI> originalUris =
                exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);

        if (originalUris != null && !originalUris.isEmpty()) {
            return originalUris.get(0).getPath();
        }

        // Fallback (should rarely happen)
        return exchange.getRequest().getURI().getPath();
    }

    /**
     * Simple role-to-path authorization rules.
     * Gateway authorization is intentionally coarse-grained.
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
