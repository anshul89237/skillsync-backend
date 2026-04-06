//package com.lpu.AuthService.service;
//
//import com.lpu.AuthService.config.JwtUtil;
//import com.lpu.AuthService.dto.LoginRequest;
//import com.lpu.AuthService.dto.LoginResponse;
//import com.lpu.AuthService.entity.Users;
//import com.lpu.AuthService.exception.InvalidCredentialsException;
//import com.lpu.AuthService.exception.UserNotFoundException;
//import com.lpu.AuthService.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//	@Mock
//	private UserRepository repository;
//
//	@Mock
//	private PasswordEncoder passwordEncoder;
//
//	@Mock
//	private JwtUtil jwtUtil;
//
//	@InjectMocks
//	private UserService userService;
//
//	private Users user;
//
//	@BeforeEach
//	void setUp() {
//		user = new Users();
//		user.setId(1L);
//		user.setName("John Doe");
//		user.setEmail("john@example.com");
//		user.setPassword("hashedPassword");
//		user.setRole("ROLE_USER");
//	}
//
//	@Test
//	void saveUser_shouldEncodePasswordAndSave() {
//		when(passwordEncoder.encode("rawPass")).thenReturn("hashedPassword");
//		when(repository.save(any(Users.class))).thenReturn(user);
//
//		Users input = new Users();
//		input.setName("John Doe");
//		input.setEmail("john@example.com");
//		input.setPassword("rawPass");
//
//		Users result = userService.saveUser(input);
//
//		assertNotNull(result);
//		assertEquals("hashedPassword", input.getPassword());
//		verify(passwordEncoder).encode("rawPass");
//		verify(repository).save(input);
//	}
//
//	
//	@Test
//	void login_shouldReturnTokenOnValidCredentials() {
//		LoginRequest request = new LoginRequest();
//		request.setEmail("john@example.com");
//		request.setPassword("rawPass");
//
//		when(repository.findUserByEmail("john@example.com")).thenReturn(user);
//		when(passwordEncoder.matches("rawPass", "hashedPassword")).thenReturn(true);
//		when(jwtUtil.generateToken("john@example.com", 1L, "ROLE_USER")).thenReturn("jwt-token");
//
//		LoginResponse response = userService.login(request);
//
//		assertNotNull(response);
//		assertEquals("jwt-token", response.getToken());
//		assertEquals("ROLE_USER", response.getRole());
//		assertEquals("john@example.com", response.getEmail());
//		assertEquals(1L, response.getUserId());
//	}
//
//	@Test
//	void login_shouldThrowUserNotFoundException_whenEmailNotFound() {
//		LoginRequest request = new LoginRequest();
//		request.setEmail("notfound@example.com");
//		request.setPassword("anyPass");
//
//		when(repository.findUserByEmail("notfound@example.com")).thenReturn(null);
//
//		assertThrows(UserNotFoundException.class, () -> userService.login(request));
//		verify(passwordEncoder, never()).matches(any(), any());
//	}
//
//	@Test
//	void login_shouldThrowInvalidCredentialsException_whenPasswordWrong() {
//		LoginRequest request = new LoginRequest();
//		request.setEmail("john@example.com");
//		request.setPassword("wrongPass");
//
//		when(repository.findUserByEmail("john@example.com")).thenReturn(user);
//		when(passwordEncoder.matches("wrongPass", "hashedPassword")).thenReturn(false);
//
//		assertThrows(InvalidCredentialsException.class, () -> userService.login(request));
//		verify(jwtUtil, never()).generateToken(any(), any(), any());
//	}
//
//
//	@Test
//	void findUserByMail_shouldReturnUser_whenEmailExists() {
//		when(repository.findUserByEmail("john@example.com")).thenReturn(user);
//
//		Users result = userService.findUserByMail("john@example.com");
//
//		assertNotNull(result);
//		assertEquals("john@example.com", result.getEmail());
//	}
//
//	@Test
//	void findUserByMail_shouldThrow_whenEmailNotFound() {
//		when(repository.findUserByEmail("missing@example.com")).thenReturn(null);
//
//		assertThrows(UserNotFoundException.class, () -> userService.findUserByMail("missing@example.com"));
//	}
//
//
//	@Test
//	void findUserById_shouldReturnUser_whenIdExists() {
//		when(repository.findById(1L)).thenReturn(Optional.of(user));
//
//		Users result = userService.findUserById(1L);
//
//		assertEquals(1L, result.getId());
//	}
//
//	@Test
//	void findUserById_shouldThrow_whenIdNotFound() {
//		when(repository.findById(99L)).thenReturn(Optional.empty());
//
//		assertThrows(UserNotFoundException.class, () -> userService.findUserById(99L));
//	}
//
//
//	@Test
//	void findUsers_shouldReturnAllUsers() {
//		when(repository.findAll()).thenReturn(List.of(user));
//
//		List<Users> users = userService.findUsers();
//
//		assertEquals(1, users.size());
//	}
//
//
//	@Test
//	void updateUserById_shouldUpdateAndReturn() {
//		Users update = new Users();
//		update.setName("Jane Doe");
//		update.setEmail("jane@example.com");
//
//		when(repository.findById(1L)).thenReturn(Optional.of(user));
//		when(repository.save(any(Users.class))).thenReturn(user);
//
//		Users result = userService.updateUserById(1L, update);
//
//		assertNotNull(result);
//		verify(repository).save(user);
//	}
//
//	@Test
//	void updateUserById_shouldThrow_whenNotFound() {
//		when(repository.findById(99L)).thenReturn(Optional.empty());
//
//		assertThrows(UserNotFoundException.class, () -> userService.updateUserById(99L, new Users()));
//	}
//
//
//	@Test
//	void deleteUserById_shouldDelete_whenFound() {
//		when(repository.findById(1L)).thenReturn(Optional.of(user));
//		doNothing().when(repository).deleteById(1L);
//
//		assertDoesNotThrow(() -> userService.deleteUserById(1L));
//
//		verify(repository).deleteById(1L);
//	}
//
//	@Test
//	void deleteUserById_shouldThrow_whenNotFound() {
//		when(repository.findById(99L)).thenReturn(Optional.empty());
//
//		assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(99L));
//		verify(repository, never()).deleteById(any());
//	}
//
//
//	@Test
//	void updateRole_shouldUpdateRole_whenUserFound() {
//		when(repository.findById(1L)).thenReturn(Optional.of(user));
//		when(repository.save(any(Users.class))).thenReturn(user);
//
//		Users result = userService.updateRole(1L, "ROLE_MENTOR");
//
//		assertEquals("ROLE_MENTOR", user.getRole());
//		verify(repository).save(user);
//	}
//
//	
//	@Test
//	void updateRole_shouldThrow_whenNotFound() {
//		when(repository.findById(99L)).thenReturn(Optional.empty());
//
//		assertThrows(UserNotFoundException.class, () -> userService.updateRole(99L, "ROLE_MENTOR"));
//	}
//}