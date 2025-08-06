package com.ll.commars.domain.auth.token.entity;

public record RefreshToken(
		String subject,
		String token,
		long expiration
) implements Token{

	public RefreshToken(String subject, String token) {
		this(subject, token, 0L);
	}
}
