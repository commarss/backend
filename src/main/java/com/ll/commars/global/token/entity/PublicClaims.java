package com.ll.commars.global.token.entity;

import java.time.Instant;
import java.util.Objects;

public record PublicClaims(
	String issuer,
	TokenSubject subject,
	Instant issuedAt,
	Instant expiresAt,
	String jti
) {

	public PublicClaims {
		Objects.requireNonNull(issuer, "issuer must not be null");
		Objects.requireNonNull(subject, "subject must not be null");
		Objects.requireNonNull(issuedAt, "issuedAt must not be null");
		Objects.requireNonNull(expiresAt, "expiresAt must not be null");
		Objects.requireNonNull(jti, "jti must not be null");
	}

	public PublicClaims(String issuer, String subject, Instant issuedAt, Instant expiresAt, String jti) {
		this(issuer, TokenSubject.of(subject), issuedAt, expiresAt, jti);
	}
}
