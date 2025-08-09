package com.ll.commars.domain.auth.dto;

public record OAuthSignInResponse(
		String accessToken,
		String refreshToken,
		boolean isRegistered
		) implements OAuthResponse {
}
