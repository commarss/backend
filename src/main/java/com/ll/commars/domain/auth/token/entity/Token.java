package com.ll.commars.domain.auth.token.entity;

public sealed interface Token permits AccessToken, RefreshToken {

	long expiration();
	String subject();
	JwtTokenValue token();
}
