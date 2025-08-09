package com.ll.commars.domain.auth.token.entity;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ll.commars.domain.member.entity.Member;

import io.jsonwebtoken.Claims;

public record JwtClaims(
	PublicClaims publicClaims,
	PrivateClaims privateClaims
) {

	public static JwtClaims ofAccessToken(Member member, Instant issuedAt, Instant expiresAt, String jti) {
		PublicClaims publicClaims = new PublicClaims("commars.com", TokenSubject.of(member.getEmail()), issuedAt, expiresAt, jti);
		PrivateClaims privateClaims = new PrivateClaims(member.getId(), List.of("ROLE_USER"));
		return new JwtClaims(publicClaims, privateClaims);
	}

	public static JwtClaims ofRefreshToken(Member member, Instant issuedAt, Instant expiresAt, String jti) {
		PublicClaims publicClaims = new PublicClaims("commars.com", TokenSubject.of(member.getEmail()), issuedAt, expiresAt, jti);
		PrivateClaims privateClaims = new PrivateClaims(member.getId(), Collections.emptyList());
		return new JwtClaims(publicClaims, privateClaims);
	}

	public static JwtClaims from(Claims claims) {
		PublicClaims publicClaims = new PublicClaims(
			claims.getIssuer(),
			claims.getSubject(),
			claims.getIssuedAt().toInstant(),
			claims.getExpiration().toInstant(),
			claims.getId()
		);
		PrivateClaims privateClaims = new PrivateClaims(
			claims.get("userId", Long.class),
			(List<String>)claims.get("roles")
		);
		return new JwtClaims(publicClaims, privateClaims);
	}

	public Map<String, Object> toClaimsMap() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("iss", publicClaims.issuer());
		claims.put("sub", publicClaims.subject().value());
		claims.put("iat", Date.from(publicClaims.issuedAt()));
		claims.put("exp", Date.from(publicClaims.expiresAt()));
		claims.put("userId", privateClaims.userId());
		claims.put("jti", publicClaims.jti());

		if (privateClaims.roles() != null && !privateClaims.roles().isEmpty()) {
			claims.put("roles", privateClaims.roles());
		}

		return claims;
	}
}
