package com.lpu.AuthService.custom.exceptions;

public class OtpNotVerifiedException extends RuntimeException {

    public OtpNotVerifiedException(String message) {
        super(message);
    }
}
