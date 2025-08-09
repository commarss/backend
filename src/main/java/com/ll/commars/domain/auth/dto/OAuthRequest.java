package com.ll.commars.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record OAuthRequest(
	@NotBlank(message = "인증 코드를 입력해주세요.")
	String authorizationCode
) {
}
