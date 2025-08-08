package com.ll.commars.domain.auth.token.entity;

public record RefreshToken(
	TokenSubject subject,
	TokenValue token,
	long expiration
) implements Token {
}
