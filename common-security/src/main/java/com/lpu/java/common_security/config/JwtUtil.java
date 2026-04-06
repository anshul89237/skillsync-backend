package com.lpu.java.common_security.config;
 
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
 
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
 
@Component
public class JwtUtil {
 
	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
 
	 private String secret = "bXktc3VwZXItc2VjcmV0LWtleS13aGljaC1pcy1sb25n";
	 long EXPIRATION_TIME = 900000l; 
 
	    public void setSecret(String secret) {
	        this.secret = secret;
	    }
 
	    public String generateToken(Long userId, String email, String role, String name) {
			logger.info("Generating new JWT for user: {}", email);
	        return Jwts.builder()
	                .setSubject(email)
	                .claim("userId", userId)
	                .claim("role", role)
	                .claim("roles", java.util.List.of(role))
	                .claim("name", name)
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	                .signWith(SignatureAlgorithm.HS256, secret)
	                .compact();
	    }
 
    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.error("Error parsing JWT claims: {}", e.getMessage());
            throw e;
        }
    }
 
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }
 
    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public java.util.List<String> getRoles(String token) {
        return getClaims(token).get("roles", java.util.List.class);
    }
 
    public boolean isExpired(String token) {
        boolean expired = getClaims(token).getExpiration().before(new Date());
		if (expired) {
			logger.warn("JWT token has expired");
		}
		return expired;
    }
 
    public boolean validateToken(String token) {
        try {
            return !isExpired(token);
        } catch (Exception e) {
            logger.error("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }
    
    public Long getUserId(String token) {
        return getClaims(token).get("userId", Long.class);
    }
 
    public String getName(String token) {
        return getClaims(token).get("name", String.class);
    }
}