package com.youcode.taskmanager.common.security.provider.jwt.service.token;

import com.youcode.taskmanager.shared.Enum.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;

public interface JwtService {
    String extractUserName(String token);
    TokenType extractTokenType(String token);
    String generaAccessToken(UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
    public void checkTokenType(String token, TokenType tokenType);
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, Date expiration, TokenType tokenType);
}