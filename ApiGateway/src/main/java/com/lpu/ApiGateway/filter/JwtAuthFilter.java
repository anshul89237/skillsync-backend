package com.lpu.ApiGateway.filter;

import com.lpu.ApiGateway.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered 
{

	@Autowired
	private JwtUtil jwtUtil;

	// Public paths — no token required
	private static final List<String> PUBLIC_PATHS = List.of("/authservice/auth/login", "/authservice/auth/register");

	// Role-based rules: path prefix + HTTP method → allowed roles
	private static final List<RoleRule> ROLE_RULES = List.of(
			new RoleRule("/groupservice/groups", "POST", List.of("ROLE_MENTOR", "ROLE_ADMIN")),
			new RoleRule("/reviewservice/reviews", "POST", List.of("ROLE_LEARNER", "ROLE_ADMIN")),
			new RoleRule("/userservice/learners", "POST", List.of("ROLE_USER", "ROLE_LEARNER", "ROLE_ADMIN")),
			new RoleRule("/mentorservice/mentors/apply", "POST", List.of("ROLE_USER", "ROLE_MENTOR", "ROLE_ADMIN")),
			new RoleRule("/sessionservice/sessions", "POST", List.of("ROLE_LEARNER", "ROLE_ADMIN")),
			new RoleRule("/sessionservice/sessions", "DELETE", List.of("ROLE_MENTOR", "ROLE_ADMIN")),
			new RoleRule("/skillservice/skills", "DELETE", List.of("ROLE_MENTOR", "ROLE_ADMIN")),
			new RoleRule("/authservice/users", "DELETE", List.of("ROLE_ADMIN")));

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) 
	{
		String path = exchange.getRequest().getURI().getPath();
		String method = exchange.getRequest().getMethod().name();

		if (isPublicPath(path)) 
		{
			return chain.filter(exchange);
		}

		String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) 
		{
			return sendError(exchange, HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
		}

		String token = authHeader.substring(7);

		if (!jwtUtil.isTokenValid(token)) 
		{
			return sendError(exchange, HttpStatus.UNAUTHORIZED, "Token is invalid or expired");
		}

		String role = jwtUtil.extractRole(token);

		if (!isRoleAllowed(path, method, role)) 
		{
			return sendError(exchange, HttpStatus.FORBIDDEN,
					"Access denied: your role '" + role + "' is not permitted for this action");
		}

		// Inject user info as headers so downstream services know who called
		ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
				.header("X-User-Id", jwtUtil.extractUserId(token)).header("X-User-Email", jwtUtil.extractEmail(token))
				.header("X-User-Role", role).build();

		return chain.filter(exchange.mutate().request(mutatedRequest).build());
	}

	private boolean isPublicPath(String path) {
		return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
	}

	private boolean isRoleAllowed(String path, String method, String role)
	{
		for (RoleRule rule : ROLE_RULES) 
		{
			if (path.startsWith(rule.pathPrefix) && rule.method.equalsIgnoreCase(method)) 
			{
				return rule.allowedRoles.contains(role);
			}
		}
		
		return true; // no specific rule → any valid token is fine
	}

	private Mono<Void> sendError(ServerWebExchange exchange, HttpStatus status, String message) 
	{
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(status);
		response.getHeaders().add("Content-Type", "application/json");
		byte[] bytes = ("{\"error\": \"" + message + "\"}").getBytes();
		var buffer = response.bufferFactory().wrap(bytes);
		return response.writeWith(Mono.just(buffer));
	}

	@Override
	public int getOrder() 
	{
		return -1;
	}

	private static class RoleRule
	{
		String pathPrefix;
		String method;
		List<String> allowedRoles;

		RoleRule(String pathPrefix, String method, List<String> allowedRoles) {
			this.pathPrefix = pathPrefix;
			this.method = method;
			this.allowedRoles = allowedRoles;
		}
	}
}