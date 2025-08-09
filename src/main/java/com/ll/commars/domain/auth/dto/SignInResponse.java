package com.ll.commars.domain.auth.dto;

public record SignInResponse(
	String accessToken,
	String refreshToken
) {
}
