package com.lpu.AuthService.service;

import com.lpu.AuthService.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createToken(String username);

    RefreshToken rotateToken(String requestToken);
}
