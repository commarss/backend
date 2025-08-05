package com.ll.commars.domain.auth.token.component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ll.commars.domain.auth.token.entity.JwtToken;
import com.ll.commars.domain.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
// todo: TokenProvider 인터페이스를 생성하고 이를 구현하는 방식으로 작성할지?
public class JwtProvider {

	@Value("${jwt.secret}")
	private String secretKey;

	public final long ACCESS_TOKEN_VALIDITY = 24 * 60 * 60 * 1000L;
	public final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000L;

	public String generateAccessToken(User user) {
		return createToken(user, ACCESS_TOKEN_VALIDITY);
	}

	public String generateRefreshToken(User user) {
		return createToken(user, REFRESH_TOKEN_VALIDITY);
	}

	private String createToken(User user, long validityInMilliseconds) {
		Date ext = new Date();
		ext.setTime(ext.getTime() + validityInMilliseconds);

		Map<String, Object> payload = new HashMap<>();
		payload.put("id", user.getId());
		payload.put("email", user.getEmail());

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

	public JwtToken extractJwtToken(String token) {
		Claims claims = getClaims(token);

		return JwtToken.builder()
			.token(token)
			.id(claims.get("id", Long.class))
			.email(claims.get("email", String.class))
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
