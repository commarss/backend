package com.ll.commars.domain.auth.token;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.ll.commars.domain.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
// todo: TokenProvider 인터페이스를 생성하고 이를 구현하는 방식으로 작성할지?
public class JwtProvider {

	private final JwtProperties jwtProperties;

	byte[] keyBytes = jwtProperties.key().getBytes(StandardCharsets.UTF_8);
	SecretKey key = Keys.hmacShaKeyFor(keyBytes);

	public String generateAccessToken(User user) {
		return generateToken(user, jwtProperties.accessTokenExpiration());
	}

	public String generateRefreshToken(User user) {
		return generateToken(user, jwtProperties.refreshTokenExpiration());
	}

	private String generateToken(User user, long expirationTime) {
		Date now = new Date();
		Date expirationDate = new Date(now.getTime() + expirationTime);

		Map<String, Object> payload = new HashMap<>();
		payload.put("id", user.getId());
		payload.put("email", user.getEmail());

		return Jwts.builder()
			.claims(payload)
			.expiration(expirationDate)
			.signWith(key)
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
			.build();
	}

	private Claims getClaims(String token) {
		return Jwts.parser()
			.verifyWith(key)
			.build().
			parseSignedClaims(token).
			getPayload();
	}
}
