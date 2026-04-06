package com.lpu.AuthService.custom.exceptions;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String msg) {
        super(msg);
    }
}
