package com.ll.commars.domain.auth.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final long validityInMilliseconds = 3600000;
    private final long refreshTokenValidity = 7 * 24 * 60 * 60 * 1000L;

    public String createToken(String email, String name, String profileImageUrl) {

        Date ext = new Date();
        ext.setTime(ext.getTime() + validityInMilliseconds);

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("name", name);
        payload.put("profileImageUrl", profileImageUrl);

        return Jwts.builder()
                .claims(payload)
                .expiration(ext)
                .signWith(getSecretKey())
                .compact();
    }

    public String createRefreshToken(String email, String name, String profileImageUrl) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("name", name);
        claims.put("profileImageUrl", profileImageUrl);

        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidity);  // 7일 유효

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(getSecretKey())
                .compact();
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
