package com.ll.commars.global.token.entity;

public record RefreshToken(
	TokenSubject subject,
	TokenValue token,
	long expiration
) implements Token {
}
