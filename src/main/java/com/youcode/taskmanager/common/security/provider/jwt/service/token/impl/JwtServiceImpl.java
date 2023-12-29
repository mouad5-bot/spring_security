package com.youcode.taskmanager.common.security.provider.jwt.service.token.impl;

import com.youcode.taskmanager.common.security.provider.jwt.service.token.JwtService;
import com.youcode.taskmanager.shared.Enum.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.micrometer.observation.annotation.Observed;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
@Data
public class JwtServiceImpl implements JwtService {

    @Value("${token.signing.key}")
    private String jwtSigningKey;

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public TokenType extractTokenType(String token) {
        return extractClaim(token, claims -> TokenType.valueOf(claims.get("tokenType", String.class)));
    }

    @Override
    public String generaAccessToken(UserDetails userDetails) {
        Date expiry_5_minute = new Date(System.currentTimeMillis() + 5L * 60 * 1000);
        return generateToken(new HashMap<>(), userDetails, expiry_5_minute, TokenType.Access);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    @Override
    public void checkTokenType(String token, TokenType tokenType) {
        extractClaim(token, claims -> {
            if (!claims.get("tokenType").equals(tokenType.name())) {
                throw new IllegalStateException("Token type is not valid");
            }
            return true;
        });
    }

    protected <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    @Observed
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, Date expiration, TokenType tokenType) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .claim("tokenType", tokenType.name())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    protected boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    protected Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    protected Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    protected Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
