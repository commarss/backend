package com.ll.commars.domain.auth.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMemberInfoDto(
	@JsonProperty("id")
	String id,

	@JsonProperty("kakao_account")
	KakaoAccount kakaoAccount
) implements OAuthMemberInfoDto {

	public KakaoMemberInfoDto {
		Objects.requireNonNull(kakaoAccount, "'kakao_account'를 찾을 수 없습니다.");
	}

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
	) {

		public KakaoAccount {
			Objects.requireNonNull(email, "이메일을 찾을 수 없습니다.");
			Objects.requireNonNull(profile, "'profile'을 찾을 수 없습니다.");
		}
	}

	private record KakaoProfile(
		String nickname
	) {

		public KakaoProfile {
			Objects.requireNonNull(nickname, "닉네임을 찾을 수 없습니다.");
		}
	}
}
