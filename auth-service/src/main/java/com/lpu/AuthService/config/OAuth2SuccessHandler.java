package com.lpu.AuthService.config;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lpu.AuthService.entity.RefreshToken;
import com.lpu.AuthService.entity.User;
import com.lpu.AuthService.repository.AuthUserRepository;
import com.lpu.AuthService.service.RefreshTokenService;
import com.lpu.java.common_security.config.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    @Autowired
    private AuthUserRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        logger.info("OAuth2 login successful for user: {}", (String) oAuth2User.getAttribute("email"));
        logger.debug("OAuth2 Attributes: {}", oAuth2User.getAttributes());

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // 🔥 SAFETY CHECKS
        if (email == null) {
            logger.error("OAuth2 login failed: Email not found from provider attributes");
            throw new RuntimeException("Email not found from Google");
        }

        if (name == null) {
            name = "User";
        }
        User user = repo.findByEmail(email).orElse(null);

        if (user == null) {
            logger.info("New OAuth2 user (Google): {}. Registering in local database...", email);
            user = new User();

            user.setEmail(email);
            user.setName(name);
            user.setActive(true);
            user.setVerified(true);
            user.setRole("ROLE_USER");

            String[] parts = name.split(" ");
            String fName = parts.length > 0 ? parts[0].replaceAll("[^A-Za-z]", "") : "User";
            String lName = parts.length > 1 ? parts[1].replaceAll("[^A-Za-z]", "") : "User";
            if (fName.length() < 2)
                fName = "User";
            if (lName.length() < 2)
                lName = "User";

            user.setFirstName(fName);
            user.setLastName(lName);

            PasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode("OAUTH_USER"));

            // 🔥 FIX PROVIDERS
            user.setProviders(new ArrayList<>());
            user.getProviders().add("GOOGLE");

            repo.save(user);
            logger.info("Successfully registered new Google user: {}", email);
        } else {
            if (user.getProviders() == null) {
                user.setProviders(new ArrayList<>());
            }

            if (!user.getProviders().contains("GOOGLE")) {
                logger.info("Existing user {} logged in via Google for the first time. Adding GOOGLE provider.", email);
                user.getProviders().add("GOOGLE");
                repo.save(user);
            } else {
                logger.debug("Existing Google user {} logged in.", email);
            }
        }

        // 🔥 Generate Access Token
        String accessToken = jwtUtil.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getName());

        // 🔥 Generate Refresh Token
        RefreshToken refreshToken = refreshTokenService.createToken(user.getEmail());
        logger.info("Tokens generated for Google user: {}. Redirecting to frontend...", email);

        // 🔥 Redirect to frontend with tokens
        String frontendUrl = "http://localhost:5173/oauth2/redirect"; // Vite dev server port
        logger.info("OAuth2 redirect: userId={}, email={}, role={}", user.getId(), user.getEmail(), user.getRole());
        String redirectUrl = frontendUrl
                + "?token=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8)
                + "&refreshToken=" + URLEncoder.encode(refreshToken.getToken(), StandardCharsets.UTF_8)
                + "&email=" + URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8)
                + "&role=" + URLEncoder.encode(user.getRole(), StandardCharsets.UTF_8)
                + "&userId=" + user.getId();

        response.sendRedirect(redirectUrl);
    }
}