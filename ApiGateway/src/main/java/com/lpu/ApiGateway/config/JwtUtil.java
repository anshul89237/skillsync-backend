package com.lpu.ApiGateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil 
{

	@Value("${jwt.secret}")
	private String secret;

	private Key getSigningKey() 
	{
		return Keys.hmacShaKeyFor(secret.getBytes());
	}

	public Claims extractAllClaims(String token) 
	{
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build().parseClaimsJws(token)
				.getBody();
	}

	public boolean isTokenValid(String token) 
	{
		try 
		{
			Claims claims = extractAllClaims(token);
			return !claims.getExpiration().before(new Date());
		} 
		catch (JwtException | IllegalArgumentException e) 
		{
			return false;
		}
	}

	public String extractRole(String token) 
	{
		return extractAllClaims(token).get("role", String.class);
	}

	public String extractEmail(String token) 
	{
		return extractAllClaims(token).getSubject();
	}

	public String extractUserId(String token) 
	{
		Object userId = extractAllClaims(token).get("userId");
		return userId != null ? String.valueOf(userId) : null;
	}
}