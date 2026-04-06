package com.lpu.java.common_security.config;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
 
@Component
public class SecurityUtils {
 
	private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);
 
    /**
     * Extracts the full UserPrincipal object from the active request context.
     * Guaranteed to be populated by JwtFilter if an authenticated request is made.
     */
    public static UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
			UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
			logger.debug("Successfully extracted UserPrincipal for user: {}", principal.getEmail());
            return principal;
        }
		logger.error("Security Context violation: No valid authenticated principal found.");
        throw new RuntimeException("Unauthorized: No valid user context found in Security Context. Ensure JWT was provided.");
    }
 
    public static Long getCurrentUserId() {
        return getCurrentUser().getUserId();
    }
 
    public static String getCurrentUserEmail() {
        return getCurrentUser().getEmail();
    }
 
    public static String getCurrentUserRole() {
        return getCurrentUser().getRole();
    }
 
    public static String getCurrentUserName() {
        return getCurrentUser().getName();
    }
}
