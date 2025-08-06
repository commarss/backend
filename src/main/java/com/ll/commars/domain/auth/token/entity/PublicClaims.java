package com.ll.commars.domain.auth.token.entity;

import java.time.Instant;

public record PublicClaims(
	String issuer,
	String subject,
	Instant issuedAt,
	Instant expiresAt
) {
}
