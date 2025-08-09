package com.ll.commars.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleMemberInfoDto(
	@JsonProperty("sub")
	String id,

	@JsonProperty("email")
	String email,

	@JsonProperty("name")
	String nickname
) implements OAuthMemberInfoDto {

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getNickname() {
		return nickname;
	}
}
