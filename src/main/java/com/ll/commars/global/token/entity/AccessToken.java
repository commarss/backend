package com.ll.commars.global.token.entity;

public record AccessToken(
	TokenSubject subject,
	TokenValue token,
	long expiration
) implements Token {
}
