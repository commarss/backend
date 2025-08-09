package com.ll.commars.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMemberInfoDto(
	@JsonProperty("id")
	String id,

	@JsonProperty("kakao_account")
	KakaoAccount kakaoAccount
) implements OAuthMemberInfoDto {

	@Override
	public String getEmail() {
		return kakaoAccount.email();
	}

	@Override
	public String getNickname() {
		return kakaoAccount.profile().nickname();
	}

	private record KakaoAccount(
		String email,

		@JsonProperty("profile")
		KakaoProfile profile
	) {}

	private record KakaoProfile(
		String nickname
	) {}
}
