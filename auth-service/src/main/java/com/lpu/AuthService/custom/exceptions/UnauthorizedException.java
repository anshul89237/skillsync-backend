package com.lpu.AuthService.custom.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String msg) {
        super(msg);
    }
}
