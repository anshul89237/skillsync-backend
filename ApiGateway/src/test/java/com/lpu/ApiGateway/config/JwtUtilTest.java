package com.lpu.ApiGateway.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest 
{

	private JwtUtil jwtUtil;
	private static final String SECRET = "anshulkumar94122skillsyncsecretkey";

	@BeforeEach
	void setUp() 
	{
		jwtUtil = new JwtUtil();
		ReflectionTestUtils.setField(jwtUtil, "secret", SECRET);
	}

	private String buildToken(long expirationMillis) 
	{
		Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
		return Jwts.builder()
				.setSubject("john@example.com")
				.claim("userId", 1L)
				.claim("role", "ROLE_USER")
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	@Test
	void isTokenValid_shouldReturnTrue_forValidToken() 
	{
		String token = buildToken(86400000L);
		assertTrue(jwtUtil.isTokenValid(token));
	}

	@Test
	void isTokenValid_shouldReturnFalse_forExpiredToken() 
	{
		String token = buildToken(-1000L);
		assertFalse(jwtUtil.isTokenValid(token));
	}

	@Test
	void isTokenValid_shouldReturnFalse_forGarbageToken() 
	{
		assertFalse(jwtUtil.isTokenValid("garbage.token.value"));
	}

	@Test
	void extractEmail_shouldReturnSubject() 
	{
		String token = buildToken(86400000L);
		assertEquals("john@example.com", jwtUtil.extractEmail(token));
	}

	@Test
	void extractRole_shouldReturnRole() 
	{
		String token = buildToken(86400000L);
		assertEquals("ROLE_USER", jwtUtil.extractRole(token));
	}

	@Test
	void extractUserId_shouldReturnUserId()
	{
		String token = buildToken(86400000L);
		assertEquals("1", jwtUtil.extractUserId(token));
	}
}