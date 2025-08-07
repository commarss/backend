package com.ll.commars.domain.auth.dto;

public record TokenReissueResponse(
	String accessToken,
	String refreshToken
) {
}
