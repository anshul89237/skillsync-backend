package com.lpu.ApiGateway.filter;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {

    private static final List<String> openApi = List.of(
            "/auth/login",
            "/auth/register",
            "/auth/send-otp",
            "/auth/verify-otp",
            "/auth/google-login",
            "/auth/linkedin-login",
            "/oauth2/",
            "/login/oauth2/"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApi.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}