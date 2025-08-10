package com.ll.commars.domain.auth.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleMemberInfoDto(
	@JsonProperty("sub")
	String id,

	@JsonProperty("email")
	String email,

	@JsonProperty("name")
	String nickname
) implements OAuthMemberInfoDto {

	public GoogleMemberInfoDto {
		Objects.requireNonNull(email, "이메일을 찾을 수 없습니다.");
		Objects.requireNonNull(nickname, "닉네임을 찾을 수 없습니다.");
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getNickname() {
		return nickname;
	}
}
