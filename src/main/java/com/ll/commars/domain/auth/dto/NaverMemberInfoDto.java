package com.ll.commars.domain.auth.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverMemberInfoDto(
	@JsonProperty("response")
	Response response
) implements OAuthMemberInfoDto {

	public NaverMemberInfoDto {
		Objects.requireNonNull(response, "'response'를 찾을 수 없습니다.");
	}

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
	) {

		public Response {
			Objects.requireNonNull(email, "이메일을 찾을 수 없습니다.");
			Objects.requireNonNull(nickname, "닉네임을 찾을 수 없습니다.");
		}
	}
}
