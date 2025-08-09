package com.ll.commars.global.token.entity;

public sealed interface Token permits AccessToken, RefreshToken {

	long expiration();

	TokenSubject subject();

	TokenValue token();
}
