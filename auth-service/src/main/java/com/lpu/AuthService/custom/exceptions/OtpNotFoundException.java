package com.lpu.AuthService.custom.exceptions;

public class OtpNotFoundException extends RuntimeException {
    public OtpNotFoundException(String message) {
        super(message);
    }
}
