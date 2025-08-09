package com.ll.commars.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverMemberInfoDto(
	@JsonProperty("response")
	Response response
) implements OAuthMemberInfoDto {

	@Override
	public String getEmail() {
		return response.email();
	}

	@Override
	public String getNickname() {
		return response.nickname();
	}

	private record Response(
		String id,
		String email,
		String nickname
	) {}
}
