package com.lpu.AuthService.custom.exceptions;

public class OtpAttemptsExceededException extends RuntimeException {
    public OtpAttemptsExceededException(String message) {
        super(message);
    }
}