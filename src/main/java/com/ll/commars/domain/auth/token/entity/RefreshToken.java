package com.ll.commars.domain.auth.token.entity;

public record RefreshToken(
	String subject,
	JwtTokenValue token,
	long expiration
) implements Token {
}
