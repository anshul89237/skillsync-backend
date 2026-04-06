package com.lpu.AuthService.custom.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String msg) {
        super(msg);
    }
}
