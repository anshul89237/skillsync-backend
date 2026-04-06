package com.lpu.java.common_security.config;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GatewayValidationFilter extends OncePerRequestFilter {

    private static final String HEADER = "X-Internal-Request";

    // 🔥 Public paths (OAuth + Auth)
    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/login",
            "/auth/register",
            "/auth/send-otp",
            "/auth/verify-otp",
            "/auth/google-login",

            // 🔥 OAuth
            "/oauth2",
            "/login/oauth2",

            // 🔥 Swagger Docs (CRITICAL)
            "/v3/api-docs",
            "/swagger-ui",
            "/actuator",   // 🔥 ADD THIS
            // fallback
            "/error"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

   
    	String path = request.getRequestURI();

    	if (path.startsWith("/oauth2") || 
    	    path.startsWith("/login/oauth2") || 
    	    path.startsWith("/login") || 
    	    path.startsWith("/auth")) {
    	    
    	    filterChain.doFilter(request, response);
    	    return;
    	}
    	
   
        String path1 = request.getRequestURI();

        // ✅ 1. Allow PUBLIC paths (VERY IMPORTANT FOR OAUTH)
        if (isPublicPath(path1)) {
            filterChain.doFilter(request, response);
            return;
        }

//        // ✅ 2. Allow internal microservice calls
//        String internal = request.getHeader("X-Internal-Call");
//        if ("true".equals(internal)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        
        String internal = request.getHeader("X-Internal-Request");

        if ("gateway".equals(internal)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔐 3. Validate Gateway header
        String header = request.getHeader(HEADER);

        if (!"gateway".equals(header)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Access only via API Gateway");
            return;
        }

        // ✅ 4. Allow request
        filterChain.doFilter(request, response);
    }

    // 🔥 Helper method
    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }
}