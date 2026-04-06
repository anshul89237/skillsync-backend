package com.lpu.ApiGateway.filter;
 
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
 
import reactor.core.publisher.Mono;
 
@Component
public class JwtFilter implements GlobalFilter {
 
	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
 
    @Autowired
    private RouteValidator validator;
 
    @Autowired
    private JwtUtil jwtUtil;
 
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
 
        String path = exchange.getRequest().getURI().getPath();
		logger.debug("ApiGateway handling request for path: {}", path);
        
        // 🔥 ADD THIS BLOCK AT TOP (VERY IMPORTANT)
        if (path.contains("oauth2") || path.contains("authorization")) {
			logger.info("OAuth2/Authorization request detected. Bypassing gateway JWT filter: {}", path);
            return chain.filter(exchange);
        }
 
 
        // ✅ PUBLIC ENDPOINTS (ONLY REQUIRED ONES)
        if (isPublicPath(path)) {
			logger.debug("Public path detected. Bypassing gateway JWT filter: {}", path);
            return chain.filter(exchange);
        }
 
        // 🔐 JWT VALIDATION
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Unauthorized access attempt to secured path: {}. Missing or invalid Authorization header.", path);
            return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        try {
            if (!jwtUtil.validateToken(token)) {
                return onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
            }

            String role = jwtUtil.getRole(token);
            logger.debug("Request path: {}, User role: {}", path, role);

            // 🛡️ COARSE-GRAINED RBAC (API GATEWAY LEVEL)
            if (isAdminPath(path) && !"ROLE_ADMIN".equals(role)) {
                logger.warn("Access denied for path: {} for user with role: {}", path, role);
                return onError(exchange, "Access Denied: Admin privileges required", HttpStatus.FORBIDDEN);
            }

        } catch (Exception e) {
            logger.error("Token validation error: {}", e.getMessage());
            return onError(exchange, "Token validation failed", HttpStatus.UNAUTHORIZED);
        }

        logger.debug("Forwarding request with Authorization header to downstream service: {}", path);
        return chain.filter(exchange);
    }

    private boolean isAdminPath(String path) {
        return path.contains("/admin/");
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        byte[] bytes = String.format("""
                {
                  "status": %d,
                  "error": "%s",
                  "message": "%s"
                }
                """, status.value(), status.getReasonPhrase(), err).getBytes(StandardCharsets.UTF_8);

        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
    
    private boolean isPublicPath(String path) {
    	return path.contains("/auth/login") ||
    	           path.contains("/auth/register") ||
    	           path.contains("/auth/send-otp") ||
    	           path.contains("/auth/verify-otp") ||
    	           path.contains("/auth/google-login") ||
 
    	           // 🔥 FIX (REAL PATHS)
    	           path.contains("/authorization") ||
    	           path.contains("/login/oauth2") ||
 
    	           path.contains("/swagger-ui") ||
    	           path.contains("/v3/api-docs") ||
 
    	           path.contains("/error");
    }
}