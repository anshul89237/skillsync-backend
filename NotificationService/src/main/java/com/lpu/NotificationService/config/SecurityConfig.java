package com.lpu.NotificationService.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lpu.java.common_security.config.GatewayValidationFilter;
import com.lpu.java.common_security.config.JwtFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final GatewayValidationFilter gatewayValidationFilter;
    private final JwtFilter jwtFilter;

    public SecurityConfig(GatewayValidationFilter gatewayValidationFilter,
                          JwtFilter jwtFilter) {
        this.gatewayValidationFilter = gatewayValidationFilter;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filter(HttpSecurity http) throws Exception {

        http
            // ❌ Disable CSRF (stateless APIs)
            .csrf(csrf -> csrf.disable())

            // ❌ No sessions (JWT-based auth)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 🔐 Authorization rules
            .authorizeHttpRequests(auth -> auth

                // ✅ Allow Swagger (important)
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()

                // ✅ Allow health check (optional)
                .requestMatchers("/actuator/**").permitAll()

                // ❌ Everything else secured
                .anyRequest().authenticated()
            )

            // 🔥 Add filters in correct order
            // 1️⃣ Block direct service access (Zero-Trust core)
            .addFilterBefore(gatewayValidationFilter,
                UsernamePasswordAuthenticationFilter.class)

            // 2️⃣ Validate JWT token
            .addFilterAfter(jwtFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🔐 Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    // 🔐 Auth manager
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}