package com.lpu.AuthService.repository;

import java.util.ArrayList;
import org.springframework.stereotype.Component;

import com.lpu.AuthService.dto.RegisterRequest;
import com.lpu.AuthService.dto.response.AuthResponse;
import com.lpu.AuthService.entity.RefreshToken;
import com.lpu.AuthService.entity.User;

@Component
public class AuthMapper {

    public User toUser(RegisterRequest req) {
        if (req == null) {
            return null;
        }

        User user = new User();
        user.setName(req.getFirstName() + " " + req.getLastName());
        user.setEmail(req.getEmail());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setRole("ROLE_USER");
        user.setProviders(new ArrayList<>());
        return user;
    }

    public AuthResponse toAuthResponse(String accessToken, RefreshToken refreshToken) {
        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken != null ? refreshToken.getToken() : null);
        return response;
    }
}
















/*
 * 👉 MAPSTRUCT MAPPING RULE
 *
 * If source and target field names are SAME → automatic mapping ✅
 * If names are DIFFERENT → use @Mapping ❗
 *
 * ------------------------------------------------------------
 * 📦 PARAMETERS EXPLANATION
 * ------------------------------------------------------------
 * source  → From where data is coming
 * target  → Where data should go (DTO field)
 *
 * ------------------------------------------------------------
 * 🚀 REAL-LIFE ANALOGY
 * ------------------------------------------------------------
 * Think like filling a form:
 *
 * Source = Where you read data from 📄
 * Target = Where you write it 📝
 *
 * Example:
 * userToken.user.username  →  JwtResponse.username
 *
 * ------------------------------------------------------------
 * ⚠️ IMPORTANT RULES
 * ------------------------------------------------------------
 * 1. Source must start with METHOD PARAMETER NAME
 *
 *    Example:
 *    toJwtResponse(String accessToken, RefreshToken userToken)
 *    → source = "userToken.user.username"
 *
 * 2. Nested fields allowed using dot (.)
 *
 *    Example:
 *    userToken.user.email
 *
 * 3. If names match → NO @Mapping needed
 *
 *    Example:
 *    accessToken → accessToken (auto mapped)
 *
 * 4. Use @Mapping ONLY when:
 *    ✔ Field names differ
 *    ✔ Nested mapping required
 *    ✔ Custom transformation needed
 *
 * ------------------------------------------------------------
 * 💡 QUICK SUMMARY
 * ------------------------------------------------------------
 * SAME NAME      → Auto mapping
 * DIFFERENT NAME → @Mapping required
 * NESTED FIELD   → @Mapping required
 *
 */