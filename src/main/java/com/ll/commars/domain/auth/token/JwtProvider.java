package com.ll.commars.domain.auth.token;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.ll.commars.domain.auth.token.entity.AccessToken;
import com.ll.commars.domain.auth.token.entity.RefreshToken;
import com.ll.commars.domain.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider implements TokenProvider {

	private final JwtProperties jwtProperties;

	byte[] keyBytes = jwtProperties.key().getBytes(StandardCharsets.UTF_8);
	SecretKey key = Keys.hmacShaKeyFor(keyBytes);

	private String generateToken(User user, long expirationMillis) {
		Instant now = Instant.now();
		Instant expiresAt = now.plus(expirationMillis, ChronoUnit.MILLIS);

		return Jwts.builder()
			.subject(user.getEmail())
			.claim("id", user.getId())
			.issuedAt(Date.from(now))
			.expiration(Date.from(expiresAt))
			.signWith(key)
			.compact();
	}

	@Override
	public AccessToken generateAccessToken(User user) {
		long expirationMillis = jwtProperties.accessTokenExpiration();
		String tokenValue = generateToken(user, expirationMillis);

		return new AccessToken(user.getEmail(), tokenValue, expirationMillis);
	}

	@Override
	public RefreshToken generateRefreshToken(User user) {
		long expirationMillis = jwtProperties.refreshTokenExpiration();
		String tokenValue = generateToken(user, expirationMillis);

		return new RefreshToken(user.getEmail(), tokenValue, expirationMillis);
	}

	public boolean validateToken(String token) {
		try {
			getClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Claims getClaims(String token) {
		return Jwts.parser()
			.verifyWith(key)
			.build().
			parseSignedClaims(token).
			getPayload();
	}
}
