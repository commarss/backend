package com.ll.commars.global.jwt.component;

import com.ll.commars.global.jwt.entity.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    @Value("${jwt.secret-key}")
    private String secretKey;

    private final long accessTokenValidity = 24 * 60 * 60 * 1000L;
    private final long refreshTokenValidity = 7 * 24 * 60 * 60 * 1000L;

    public String generateAccessToken(long id, String email, String name, String profileImageUrl) {
        return createToken(id, email, name, profileImageUrl, accessTokenValidity);
    }

    public String generateRefreshToken(long id, String email, String name, String profileImageUrl) {
        return createToken(id, email, name, profileImageUrl, refreshTokenValidity);
    }

    private String createToken(long id, String email, String name, String profileImageUrl, long validityInMilliseconds) {
        Date ext = new Date();
        ext.setTime(ext.getTime() + validityInMilliseconds);

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", id);
        payload.put("email", email);
        payload.put("name", name);
        payload.put("profileImageUrl", profileImageUrl);

        return Jwts.builder()
                .claims(payload)
                .expiration(ext)
                .signWith(getSecretKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public JwtToken getJwtToken(String token) {
        Claims claims = getClaims(token);

        return JwtToken.builder()
                .token(token)
                .id(claims.get("id", Long.class))
                .email(claims.get("email", String.class))
                .name(claims.get("name", String.class))
                .profileImageUrl(claims.get("profileImageUrl", String.class))
                .build();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build().
                parseSignedClaims(token).
                getPayload();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
