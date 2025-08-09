package com.ll.commars.domain.auth.token.entity;

public record AccessToken(
	TokenSubject subject,
	TokenValue token,
	long expiration
) implements Token {
}
