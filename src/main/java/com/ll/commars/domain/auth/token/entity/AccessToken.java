package com.ll.commars.domain.auth.token.entity;

public record AccessToken(
		String subject,
		String token,
		long expiration
) implements Token {

	public AccessToken(String subject, String token) {
		this(subject, token, 0L);
	}
}
