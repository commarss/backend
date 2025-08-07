package com.ll.commars.domain.auth.token.entity;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ll.commars.domain.member.entity.Member;

import io.jsonwebtoken.Claims;

public record JwtClaims(
	PublicClaims publicClaims,
	PrivateClaims privateClaims
) {

	public static JwtClaims from(Claims claims) {
		PublicClaims publicClaims = new PublicClaims(
			claims.getIssuer(),
			claims.getSubject(),
			claims.getIssuedAt().toInstant(),
			claims.getExpiration().toInstant()
		);
		PrivateClaims privateClaims = new PrivateClaims(
			claims.get("userId", Long.class),
			(List<String>)claims.get("roles")
		);
		return new JwtClaims(publicClaims, privateClaims);
	}

	public static JwtClaims from(Member member, Instant issuedAt, Instant expiresAt) {
		PublicClaims publicClaims = new PublicClaims(
			"commars.com",
			member.getEmail(),
			issuedAt,
			expiresAt
		);
		PrivateClaims privateClaims = new PrivateClaims(
			member.getId(),
			List.of("ROLE_USER") // todo: 매직 상수 별도 관리
		);
		return new JwtClaims(publicClaims, privateClaims);
	}

	public Map<String, Object> toClaimsMap() {
		return Map.of(
			"iss", publicClaims.issuer(),
			"sub", publicClaims.subject(),
			"iat", Date.from(publicClaims.issuedAt()),
			"exp", Date.from(publicClaims.expiresAt()),
			"userId", privateClaims.userId(),
			"roles", privateClaims.roles()
		);
	}
}
