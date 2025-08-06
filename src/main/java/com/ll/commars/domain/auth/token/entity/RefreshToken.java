package com.ll.commars.domain.auth.token.entity;

public record RefreshToken(
		String subject,
		JwtTokenValue token,
		long expiration
) implements Token{

	public RefreshToken(String subject, JwtTokenValue token) {
		this(subject, token, 0L);
	}
}
