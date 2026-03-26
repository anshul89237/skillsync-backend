package com.lpu.AuthService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpu.AuthService.dto.LoginRequest;
import com.lpu.AuthService.dto.LoginResponse;
import com.lpu.AuthService.entity.Users;
import com.lpu.AuthService.exception.GlobalExceptionHandler;
import com.lpu.AuthService.exception.InvalidCredentialsException;
import com.lpu.AuthService.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	private MockMvc mockMvc;

	@Mock
	private UserService userService;

	@InjectMocks
	private AuthController authController;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(authController).setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}


	@Test
	void register_shouldReturn201_whenValidUser() throws Exception {
		Users input = new Users();
		input.setName("John Doe");
		input.setEmail("john@example.com");
		input.setPassword("password123");

		Users saved = new Users();
		saved.setId(1L);
		saved.setName("John Doe");
		saved.setEmail("john@example.com");
		saved.setRole("ROLE_USER");

		when(userService.saveUser(any(Users.class))).thenReturn(saved);

		mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.email").value("john@example.com"))
				.andExpect(jsonPath("$.role").value("ROLE_USER"));
	}

	@Test
	void register_shouldReturn400_whenNameIsBlank() throws Exception {
		Users input = new Users();
		input.setName("");
		input.setEmail("john@example.com");
		input.setPassword("password123");

		mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.name").exists());
	}

	@Test
	void register_shouldReturn400_whenEmailIsInvalid() throws Exception {
		Users input = new Users();
		input.setName("John");
		input.setEmail("not-an-email");
		input.setPassword("password123");

		mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.email").exists());
	}

	@Test
	void register_shouldReturn400_whenPasswordTooShort() throws Exception {
		Users input = new Users();
		input.setName("John");
		input.setEmail("john@example.com");
		input.setPassword("123");

		mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.password").exists());
	}


	@Test
	void login_shouldReturn200_withToken_whenValidCredentials() throws Exception {
		LoginRequest request = new LoginRequest();
		request.setEmail("john@example.com");
		request.setPassword("password123");

		LoginResponse response = new LoginResponse("jwt-token", "ROLE_USER", "john@example.com", 1L);

		when(userService.login(any(LoginRequest.class))).thenReturn(response);

		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(jsonPath("$.token").value("jwt-token")).andExpect(jsonPath("$.role").value("ROLE_USER"))
				.andExpect(jsonPath("$.email").value("john@example.com"));
	}

	
	@Test
	void login_shouldReturn401_whenInvalidCredentials() throws Exception {
		LoginRequest request = new LoginRequest();
		request.setEmail("john@example.com");
		request.setPassword("wrongPass");

		when(userService.login(any(LoginRequest.class)))
				.thenThrow(new InvalidCredentialsException("Invalid email or password"));

		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.error").value("Invalid email or password"));
	}

	
	@Test
	void login_shouldReturn400_whenEmailMissing() throws Exception {
		LoginRequest request = new LoginRequest();
		request.setEmail("");
		request.setPassword("password123");

		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.email").exists());
	}
}