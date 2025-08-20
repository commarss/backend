package com.ll.commars.domain.auth.dto;

public record SignUpResponse(
	Long memberId,
	String email
) {
}
