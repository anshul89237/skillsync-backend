package com.lpu.ApiGateway.filter;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    // 🔥 Get all claims
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    // 🔥 Get email (subject)
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    // 🔥 Get role
    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // 🔥 Get userId
    public Long getUserId(String token) {
        Object userId = getClaims(token).get("userId");

        // ⚠️ Handle Integer vs Long issue
        if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        }
        return (Long) userId;
    }

    // 🔥 Check expiry
    public boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // 🔥 Validate token
    public boolean validateToken(String token) {
        try {
            return !isExpired(token);
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token");
        }
    }
}