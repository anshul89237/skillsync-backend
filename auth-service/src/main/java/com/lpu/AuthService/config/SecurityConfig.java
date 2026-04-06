package com.lpu.AuthService.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lpu.AuthService.security.CustomAccessDeniedHandler;
import com.lpu.AuthService.security.CustomAuthenticationEntryPoint;
import com.lpu.java.common_security.config.GatewayValidationFilter;
import com.lpu.java.common_security.config.HeaderAuthenticationFilter;
import com.lpu.java.common_security.config.JwtFilter;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
public class SecurityConfig 
{

	@Autowired
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	
	@Autowired
	private CustomAccessDeniedHandler customAccessDeniedHandler;
	
	@Autowired
	private HeaderAuthenticationFilter headerAuthenticationFilter;
	
	@Autowired
	private GatewayValidationFilter gatewayValidationFilter;
	
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Autowired
	private OAuth2SuccessHandler oAuth2SuccessHandler;

	@Autowired
	private OAuth2FailureHandler oAuth2FailureHandler;
	

		 
		 @Bean
		 public SecurityFilterChain filter(HttpSecurity http) throws Exception {

		     http.csrf(csrf -> csrf.disable());

		     http.authorizeHttpRequests(auth ->
		     auth.requestMatchers(
		             "/api/v1/auth/**",
		             "/auth/**",
		             "/oauth2/**",          
		             "/login/oauth2/**",    
		             "/actuator/**",                            
		             "/oauth2/code/**",
		             "/authorization/**",
		             "/v3/api-docs/**",
		             "/error"
		     ).permitAll()
		     .requestMatchers(HttpMethod.GET, "/api/v1/users/email/**").permitAll() 
		     .requestMatchers(HttpMethod.GET, "/api/v1/users/*").permitAll()
		     .anyRequest().authenticated()
		 );

		     http.sessionManagement(session ->
		     session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		 );
		     
		     http.oauth2Login(oauth -> oauth
		    		    .successHandler(oAuth2SuccessHandler)
		    		.failureHandler(oAuth2FailureHandler)
		    		);

//		      🔐 Block direct access
		     http.addFilterBefore(gatewayValidationFilter,
		             UsernamePasswordAuthenticationFilter.class);

//		     // 🔥 Build UserPrincipal (for protected APIs)
//		     http.addFilterAfter(headerAuthenticationFilter,
//		             UsernamePasswordAuthenticationFilter.class);
		      
		      // ✅ SECOND: JWT validation
		       http.addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);
			
		     
		     http
		        .exceptionHandling(ex -> ex
		            .authenticationEntryPoint(customAuthenticationEntryPoint)
		            .accessDeniedHandler(customAccessDeniedHandler)
		        );
		     

		  
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() 
	{
		return new BCryptPasswordEncoder(12);
	}
	
	@Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }
	
	
}
