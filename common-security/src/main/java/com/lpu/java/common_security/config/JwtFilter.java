package com.lpu.java.common_security.config;
 
import java.io.IOException;
import java.util.List;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
 
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
 
@Component
public class JwtFilter extends OncePerRequestFilter {
 
	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
 
    @Autowired
    private JwtUtil jwtUtil;
 
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
 
        String header = request.getHeader("Authorization");
        String path = request.getRequestURI();
 
		logger.debug("Processing request filter for path: {}", path);
 
        if (path.startsWith("/oauth2") || 
        	    path.startsWith("/login/oauth2") || 
        	    path.startsWith("/actuator") ||
        	    path.startsWith("/v3/api-docs") ||
        	    path.startsWith("/swagger-ui")) {
 
				logger.debug("Skipping JWT verification for public/infrastructure path: {}", path);
        	    chain.doFilter(request, response);
        	    return;
        	}
        
 
        // ✅ Check if Authorization header exists
        if (header != null && header.startsWith("Bearer ")) {
 
            String token = header.substring(7);
 
            try {
                // ✅ Validate token
                if (jwtUtil.validateToken(token)) {
 
                    Long userId = jwtUtil.getUserId(token);
                    String email = jwtUtil.getUsername(token);
                    String role = jwtUtil.getRole(token);
                    String name = jwtUtil.getName(token);
                    List<String> rolesList = jwtUtil.getRoles(token);
                    if (rolesList == null) {
                        rolesList = java.util.List.of(role);
                    }

                    List<SimpleGrantedAuthority> authorities = rolesList.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    logger.info("JWT validated successfully for user: {} (Roles: {})", email, rolesList);
 
                    // ✅ Wrap request to add custom headers
                    HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request) {
 
                        @Override
                        public String getHeader(String nameHeader) {
                            if ("X-User-Id".equalsIgnoreCase(nameHeader)) return String.valueOf(userId);
                            if ("X-User-Email".equalsIgnoreCase(nameHeader)) return email;
                            if ("X-User-Role".equalsIgnoreCase(nameHeader)) return role;
                            if ("X-User-Name".equalsIgnoreCase(nameHeader)) return name;
                            return super.getHeader(nameHeader);
                        }
                    };
 
                    // ✅ Create principal
                    UserPrincipal principal = new UserPrincipal(userId, email, role, name);
 
                    // ✅ Set authentication
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    principal,
                                    token, // 🔥 IMPORTANT: store token here for Feign
                                    authorities
                            );
 
                    SecurityContextHolder.getContext().setAuthentication(auth);
					logger.debug("SecurityContext populated with UserPrincipal for user: {}", email);
 
                    // ✅ VERY IMPORTANT: pass wrapped request
                    chain.doFilter(wrappedRequest, response);
                    return;
                } else {
					logger.warn("JWT validation failed for token prefix: {}", token.substring(0, 10));
				}
 
            } catch (Exception e) {
				logger.error("JWT Processing Error on path {}: {}", path, e.getMessage());
            }
        } else {
			logger.debug("No Bearer token found in request headers for path: {}", path);
		}
 
        
        // ✅ Continue filter chain normally if no JWT
        chain.doFilter(request, response);
    }
}