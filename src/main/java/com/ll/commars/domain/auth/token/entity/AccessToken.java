package com.ll.commars.domain.auth.token.entity;

public record AccessToken(
		String subject,
		JwtTokenValue token,
		long expiration
) implements Token {
}
