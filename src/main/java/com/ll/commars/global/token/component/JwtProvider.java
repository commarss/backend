package com.ll.commars.global.token.component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.ll.commars.global.token.JwtProperties;
import com.ll.commars.global.token.TokenProvider;
import com.ll.commars.global.token.entity.AccessToken;
import com.ll.commars.global.token.entity.JwtClaims;
import com.ll.commars.global.token.entity.RefreshToken;
import com.ll.commars.global.token.entity.TokenSubject;
import com.ll.commars.global.token.entity.TokenValue;
import com.ll.commars.domain.member.entity.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider implements TokenProvider {

	private final JwtProperties jwtProperties;
	private final SecretKey secretKey;
	private final JwtParser jwtParser;

	public JwtProvider(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
		this.secretKey = Keys.hmacShaKeyFor(jwtProperties.key().getBytes());
		this.jwtParser = Jwts.parser().verifyWith(this.secretKey).build();
	}

	private TokenValue generateTokenValue(JwtClaims jwtClaims) {
		return TokenValue.of(Jwts.builder()
			.claims(jwtClaims.toClaimsMap())
			.signWith(secretKey)
			.compact());
	}

	@Override
	public AccessToken generateAccessToken(Member member) {
		TokenSubject subject = TokenSubject.of(member.getEmail());

		String jti = UUID.randomUUID().toString();

		Instant now = Instant.now();
		long expirationMillis = jwtProperties.accessTokenExpiration();
		Instant expiresAt = now.plus(expirationMillis, ChronoUnit.MILLIS);

		JwtClaims jwtClaims = JwtClaims.ofAccessToken(member, now, expiresAt, jti);
		TokenValue tokenValue = generateTokenValue(jwtClaims);

		return new AccessToken(subject, tokenValue, expirationMillis);
	}

	@Override
	public RefreshToken generateRefreshToken(Member member) {
		TokenSubject subject = TokenSubject.of(member.getEmail());

		String jti = UUID.randomUUID().toString();

		Instant now = Instant.now();
		long expirationMillis = jwtProperties.refreshTokenExpiration();
		Instant expiresAt = now.plus(expirationMillis, ChronoUnit.MILLIS);

		JwtClaims jwtClaims = JwtClaims.ofRefreshToken(member, now, expiresAt, jti);
		TokenValue tokenValue = generateTokenValue(jwtClaims);

		return new RefreshToken(subject, tokenValue, expirationMillis);
	}

	@Override
	public JwtClaims parseClaim(TokenValue tokenValue) {
		Claims claims = jwtParser
			.parseSignedClaims(tokenValue.value())
			.getPayload();

		return JwtClaims.from(claims);
	}
}
