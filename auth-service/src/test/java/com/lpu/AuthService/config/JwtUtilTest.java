//package com.lpu.AuthService.config;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class JwtUtilTest {
//
//	private JwtUtil jwtUtil;
//
//	@BeforeEach
//	void setUp() 
//	{
//		jwtUtil = new JwtUtil();
//		ReflectionTestUtils.setField(jwtUtil, "secret", "anshulkumar94122skillsyncsecretkey");
//		ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
//	}
//
//	@Test
//	void generateToken_shouldReturnNonNullToken() 
//	{
//		String token = jwtUtil.generateToken("john@example.com", 1L, "ROLE_USER");
//		assertNotNull(token);
//		assertFalse(token.isEmpty());
//	}
//
//	@Test
//	void extractEmail_shouldReturnCorrectEmail() 
//	{
//		String token = jwtUtil.generateToken("john@example.com", 1L, "ROLE_USER");
//		assertEquals("john@example.com", jwtUtil.extractEmail(token));
//	}
//
//	@Test
//	void extractRole_shouldReturnCorrectRole() {
//		String token = jwtUtil.generateToken("john@example.com", 1L, "ROLE_MENTOR");
//		assertEquals("ROLE_MENTOR", jwtUtil.extractRole(token));
//	}
//
//	@Test
//	void extractUserId_shouldReturnCorrectId() 
//	{
//		String token = jwtUtil.generateToken("john@example.com", 5L, "ROLE_USER");
//		assertEquals(5L, jwtUtil.extractUserId(token));
//	}
//
//	@Test
//	void isTokenValid_shouldReturnTrue_forFreshToken() 
//	{
//		String token = jwtUtil.generateToken("john@example.com", 1L, "ROLE_USER");
//		assertTrue(jwtUtil.isTokenValid(token));
//	}
//
//	@Test
//	void isTokenValid_shouldReturnFalse_forTamperedToken() 
//	{
//		assertFalse(jwtUtil.isTokenValid("this.is.not.a.valid.token"));
//	}
//
//	@Test
//	void isTokenValid_shouldReturnFalse_forExpiredToken() 
//	{
//		JwtUtil expiredUtil = new JwtUtil();
//		ReflectionTestUtils.setField(expiredUtil, "secret", "anshulkumar94122skillsyncsecretkey");
//		
//		ReflectionTestUtils.setField(expiredUtil, "expiration", -1000L);
//
//		String expiredToken = expiredUtil.generateToken("john@example.com", 1L, "ROLE_USER");
//		assertFalse(expiredUtil.isTokenValid(expiredToken));
//	}
//}