package com.ll.commars.domain.auth.token.entity;

import java.time.Instant;

public record PublicClaims(
	String issuer,
	TokenSubject subject,
	Instant issuedAt,
	Instant expiresAt
) {

	public PublicClaims(String issuer, String subject, Instant issuedAt, Instant expiresAt) {
		this(issuer, TokenSubject.of(subject), issuedAt, expiresAt);
	}
}
