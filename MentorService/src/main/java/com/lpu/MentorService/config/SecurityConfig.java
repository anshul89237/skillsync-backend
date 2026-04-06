package com.lpu.MentorService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lpu.java.common_security.config.GatewayValidationFilter;
import com.lpu.java.common_security.config.JwtFilter;

@Configuration(proxyBeanMethods = false)
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
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/actuator/**"
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/mentors/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(gatewayValidationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
