package com.lpu.AuthService.custom.exceptions;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String msg) {
        super(msg);
    }
}
