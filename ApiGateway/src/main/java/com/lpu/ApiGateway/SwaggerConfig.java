package com.lpu.ApiGateway;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {


	@Bean
	@org.springframework.context.annotation.Primary
	public OpenAPI apiGatewayOpenAPI() {
	    return new OpenAPI()
	        .servers(List.of(
	            new Server().url("http://localhost:8080")   // 🔥 FORCE GATEWAY
	        ))
	        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
	        .components(new Components()
	            .addSecuritySchemes("bearerAuth",
	                new SecurityScheme()
	                    .name("Authorization")
	                    .type(SecurityScheme.Type.HTTP)
	                    .scheme("bearer")
	                    .bearerFormat("JWT")));
	}
    
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}