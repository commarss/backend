package com.ll.commars.domain.auth.dto;

public record OAuthSignUpResponse(
		String accessToken,
		String refreshToken,
		boolean isRegistered
		) implements OAuthResponse {
}
