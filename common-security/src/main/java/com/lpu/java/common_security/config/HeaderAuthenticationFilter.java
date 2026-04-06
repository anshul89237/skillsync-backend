package com.lpu.java.common_security.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/register",
            "/auth/login",
            "/auth/verify-otp",
            "/auth/send-otp"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain chain)
            throws IOException, ServletException {

        String path = request.getRequestURI();

        // 🔥 SKIP FOR PUBLIC APIs
        for (String p : PUBLIC_PATHS) {
            if (path.startsWith(p)) {
                chain.doFilter(request, response);
                return;
            }
        }

        String userId = request.getHeader("X-User-Id");
        String email = request.getHeader("X-User-Email");
        String role = request.getHeader("X-User-Role");
        String name = request.getHeader("X-User-Name");

        if (userId != null && email != null && role != null) {

            UserPrincipal principal =
                    new UserPrincipal(Long.valueOf(userId), email, role, name);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            List.of(new SimpleGrantedAuthority(role))
                    );

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }
}