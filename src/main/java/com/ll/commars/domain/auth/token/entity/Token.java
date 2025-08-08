package com.ll.commars.domain.auth.token.entity;

public sealed interface Token permits AccessToken, RefreshToken {

	long expiration();

	TokenSubject subject();

	TokenValue token();
}
