package com.ll.commars.domain.auth.token;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.ll.commars.domain.auth.token.entity.AccessToken;
import com.ll.commars.domain.auth.token.entity.JwtClaims;
import com.ll.commars.domain.auth.token.entity.JwtTokenValue;
import com.ll.commars.domain.auth.token.entity.RefreshToken;
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

	private JwtTokenValue generateToken(Member member, long expirationMillis) {
		Instant now = Instant.now();
		Instant expiresAt = now.plus(expirationMillis, ChronoUnit.MILLIS);

		JwtClaims jwtClaims = JwtClaims.from(member, now, expiresAt);

		return JwtTokenValue.of(Jwts.builder()
			.claims(jwtClaims.toClaimsMap())
			.signWith(secretKey)
			.compact());
	}

	@Override
	public AccessToken generateAccessToken(Member member) {
		long expirationMillis = jwtProperties.accessTokenExpiration();
		JwtTokenValue tokenValue = generateToken(member, expirationMillis);

		return new AccessToken(member.getEmail(), tokenValue, expirationMillis);
	}

	@Override
	public RefreshToken generateRefreshToken(Member member) {
		long expirationMillis = jwtProperties.refreshTokenExpiration();
		JwtTokenValue tokenValue = generateToken(member, expirationMillis);

		return new RefreshToken(member.getEmail(), tokenValue, expirationMillis);
	}

	@Override
	public JwtClaims parseClaim(JwtTokenValue tokenValue) {
		Claims claims = jwtParser
			.parseSignedClaims(tokenValue.value())
			.getPayload();

		return JwtClaims.from(claims);
	}
}
