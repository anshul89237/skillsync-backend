package com.lpu.AuthService.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lpu.AuthService.custom.exceptions.AccessDeniedException;
import com.lpu.AuthService.custom.exceptions.InvalidCredentialsException;
import com.lpu.AuthService.custom.exceptions.InvalidOtpException;
import com.lpu.AuthService.custom.exceptions.InvalidTokenException;
import com.lpu.AuthService.custom.exceptions.OtpAttemptsExceededException;
import com.lpu.AuthService.custom.exceptions.OtpExpiredException;
import com.lpu.AuthService.custom.exceptions.OtpNotFoundException;
import com.lpu.AuthService.custom.exceptions.OtpNotVerifiedException;
import com.lpu.AuthService.custom.exceptions.TokenExpiredException;
import com.lpu.AuthService.custom.exceptions.UnauthorizedException;
import com.lpu.AuthService.custom.exceptions.UserAlreadyExistsException;
import com.lpu.AuthService.custom.exceptions.UserNotFoundException;
import com.lpu.java.common_security.dto.ApiResponse;
import com.lpu.AuthService.standard.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;


@RestControllerAdvice
public class GlobalExceptionHandler {

	private ErrorResponse buildErrorResponse(HttpStatus status, String error, String message,
			HttpServletRequest request) {

		ErrorResponse err = new ErrorResponse();
		err.setError(error);
		err.setStatus(status.value());
		err.setMessage(message);
		err.setPath(request.getRequestURI());
		err.setMethod(request.getMethod());
		err.setTimestamp(LocalDateTime.now().toString());
		err.setTraceId(UUID.randomUUID().toString());
		return err;
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleConstraintException(ConstraintViolationException ex,
			HttpServletRequest request) {

		Map<String, List<String>> validationErrors = new HashMap<>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			String field = violation.getPropertyPath().toString();
			// Extract only field name
			field = field.substring(field.lastIndexOf('.') + 1);
			String message = violation.getMessage();
			validationErrors.computeIfAbsent(field, key -> new ArrayList<>()).add(message);
		}
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErrorResponse errorResponse = buildErrorResponse(status, "VALIDATION_ERROR", status.toString(), request);
		errorResponse.setValidationErrors(validationErrors); // ✅ List<String>

		ApiResponse<ErrorResponse> api = new ApiResponse<>("ERROR",
				"Constraint violation from Path-Variable and Query-Params ", errorResponse);

		return new ResponseEntity<>(api, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationErrors(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		Map<String, List<String>> validationErrors = new HashMap<>();

		ex.getBindingResult().getFieldErrors().forEach(error -> {

			String field = error.getField();
			String message = error.getDefaultMessage();

			validationErrors.computeIfAbsent(field, key -> new ArrayList<>()).add(message);
		});

		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErrorResponse errorResponse = buildErrorResponse(status, "VALIDATION_ERROR", status.toString(), request);

		errorResponse.setValidationErrors(validationErrors); // 🔥 now List<String>

		ApiResponse<ErrorResponse> api = new ApiResponse<>("ERROR", "Validation failed from Request Body",
				errorResponse);

		return new ResponseEntity<>(api, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleRuntimeException(RuntimeException ex,
			HttpServletRequest request) {

		ex.printStackTrace(); // 🔥 ADD THIS

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ErrorResponse errorResponse = buildErrorResponse(status, "SERVER_ERROR", status.toString(), request);

		ApiResponse<ErrorResponse> api = new ApiResponse<>("ERROR", "FROM RUN TIME EXCEPTION CLASS", errorResponse);
		return new ResponseEntity<>(api, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleGenericException(Exception ex, HttpServletRequest request) throws Exception {

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;


	    String path = request.getRequestURI();
	    // 🔥 VERY IMPORTANT FIX
	    if (path.contains("/v3/api-docs")) {
	        throw ex; // let Spring handle it
	    }

		ErrorResponse errorResponse = buildErrorResponse(status, "SERVER_ERROR", status.toString(), request);
		ApiResponse<ErrorResponse> api = new ApiResponse<>("ERROR", "FROM EXCEPTION CLASS", errorResponse);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(api);
	}

	// 🔴 User Already Exists
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleUserExists(UserAlreadyExistsException ex,
			HttpServletRequest req) {

		HttpStatus status = HttpStatus.CONFLICT;

		return new ResponseEntity<>(
				new ApiResponse<>("ERROR", "Account already exists. Please log in or use a different email",
						buildErrorResponse(status, "USER_EXISTS", ex.getMessage(), req)),
				status);
	}

	// 🔴 User Not Found
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleUserNotFound(UserNotFoundException ex,
			HttpServletRequest req) {

		HttpStatus status = HttpStatus.NOT_FOUND;

		return new ResponseEntity<>(new ApiResponse<>("ERROR", "We couldn't find an account with the provided details",
				buildErrorResponse(status, "USER_NOT_FOUND", ex.getMessage(), req)), status);
	}

	// 🔴 OTP Not Verified
	@ExceptionHandler(OtpNotVerifiedException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleOtpNotVerified(OtpNotVerifiedException ex,
			HttpServletRequest req) {

		HttpStatus status = HttpStatus.BAD_REQUEST;

		return new ResponseEntity<>(
				new ApiResponse<>("ERROR", "OTP verification pending. Please complete verification to continue",
						buildErrorResponse(status, "OTP_NOT_VERIFIED", ex.getMessage(), req)),
				status);
	}

	// 🔴 OTP Expired
	@ExceptionHandler(OtpExpiredException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleOtpExpired(OtpExpiredException ex, HttpServletRequest req) {

		HttpStatus status = HttpStatus.BAD_REQUEST;

		return new ResponseEntity<>(
				new ApiResponse<>("ERROR", "The OTP has expired. Please request a new code to continue",
						buildErrorResponse(status, "OTP_EXPIRED", ex.getMessage(), req)),
				status);
	}

	// 🔴 Invalid OTP
	@ExceptionHandler(InvalidOtpException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleInvalidOtp(InvalidOtpException ex, HttpServletRequest req) {

		HttpStatus status = HttpStatus.BAD_REQUEST;

		return new ResponseEntity<>(new ApiResponse<>("ERROR", "Invalid OTP. Please enter the correct code",
				buildErrorResponse(status, "INVALID_OTP", ex.getMessage(), req)), status);
	}

	@ExceptionHandler(OtpAttemptsExceededException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleAttemptsExceeded(OtpAttemptsExceededException ex,
			HttpServletRequest req) {

		HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;

		return new ResponseEntity<>(new ApiResponse<>("ERROR",
				"Too many incorrect attempts. Please wait before trying again or request a new OTP",
				buildErrorResponse(status, "OTP_ATTEMPTS_EXCEEDED", ex.getMessage(), req)), status);
	}

	@ExceptionHandler(OtpNotFoundException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleOtpNotFound(OtpNotFoundException ex,
			HttpServletRequest req) {

		HttpStatus status = HttpStatus.NOT_FOUND;

		return new ResponseEntity<>(new ApiResponse<>("ERROR", "OTP not available. Please generate a new OTP",
				buildErrorResponse(status, "OTP_NOT_FOUND", ex.getMessage(), req)), status);
	}

	// ================= AUTH =================

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleInvalidCredentials(InvalidCredentialsException ex,
			HttpServletRequest req) {

		HttpStatus status = HttpStatus.UNAUTHORIZED;

		return new ResponseEntity<>(new ApiResponse<>("ERROR", "Authentication failed. Invalid username or password",
				buildErrorResponse(status, "INVALID_CREDENTIALS", ex.getMessage(), req)), status);
	}

	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleTokenExpired(TokenExpiredException ex,
			HttpServletRequest req) {

		HttpStatus status = HttpStatus.UNAUTHORIZED;

		return new ResponseEntity<>(
				new ApiResponse<>("ERROR", "Authentication token has expired. Please re-authenticate",
						buildErrorResponse(status, "TOKEN_EXPIRED", ex.getMessage(), req)),
				status);
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleInvalidToken(InvalidTokenException ex,
			HttpServletRequest req) {

		HttpStatus status = HttpStatus.UNAUTHORIZED;

		return new ResponseEntity<>(new ApiResponse<>("ERROR", "Session expired. Kindly log in again to continue",
				buildErrorResponse(status, "TOKEN_EXPIRED", ex.getMessage(), req)), status);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleUnauthorized(UnauthorizedException ex,
			HttpServletRequest req) {

		return new ResponseEntity<>(
				new ApiResponse<>("ERROR", "You are not logged in. Please authenticate to continue",
						buildErrorResponse(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", ex.getMessage(), req)),
				HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleAccessDenied(AccessDeniedException ex,
			HttpServletRequest req) {

		return new ResponseEntity<>(
				new ApiResponse<>("ERROR", "You are logged in, but you are not allowed to access this feature",
						buildErrorResponse(HttpStatus.FORBIDDEN, "ACCESS_DENIED", ex.getMessage(), req)),
				HttpStatus.FORBIDDEN);
	}

}
