package com.ll.commars.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverTokenResponse(
	@JsonProperty("access_token")
	String accessToken
) {
}
