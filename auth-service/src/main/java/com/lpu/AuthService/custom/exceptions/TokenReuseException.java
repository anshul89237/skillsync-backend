package com.lpu.AuthService.custom.exceptions;

public class TokenReuseException extends RuntimeException {
    public TokenReuseException(String msg) {
        super(msg);
    }
}
