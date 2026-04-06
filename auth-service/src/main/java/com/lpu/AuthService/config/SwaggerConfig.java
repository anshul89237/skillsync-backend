package com.lpu.AuthService.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;



@Configuration
public class SwaggerConfig {

    @Bean
    @org.springframework.context.annotation.Primary
    public OpenAPI authServiceOpenAPI() {

        final String scheme = "bearerAuth";

        return new OpenAPI()
                .servers(List.of(
                		new Server().url("http://localhost:8080/auth-service")
                ))

                // ❌ NO global security

                .components(new Components()
                        .addSecuritySchemes(scheme,
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}