package com.lpu.AuthService.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException ex) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String path = request.getRequestURI();
        String message = (ex.getMessage() != null) ? ex.getMessage() : "Unauthorized";

        String json = """
        {
            "status": 401,
            "error": "Unauthorized",
            "message": "%s",
            "path": "%s",
            "timestamp": "%s"
        }
        """.formatted(message, path, java.time.LocalDateTime.now());

        response.getWriter().write(json);
    }
}