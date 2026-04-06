package com.lpu.AuthService.custom.exceptions;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String msg) {
        super(msg);
    }
}
