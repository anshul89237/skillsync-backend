package com.lpu.java.common_security.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:SkillSync Microservice}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(applicationName + " API")
                        .description("Automated API Documentation for " + applicationName)
                        .version("1.0")
                        .contact(new Contact().name("SkillSync Ecosystem Team")))
                // 🔥 Globally injects Bearer Authentication Requirement to all endpoints
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        // 🔐 Defines the Bearer Auth logic
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter your JWT token (without 'Bearer ' prefix).")
                        ));
    }
}
