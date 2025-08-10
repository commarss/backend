package com.ll.commars.domain.auth.dto;

public record OAuthResponse(
	String accessToken,
	String refreshToken,
	boolean isRegistered
) {
}
