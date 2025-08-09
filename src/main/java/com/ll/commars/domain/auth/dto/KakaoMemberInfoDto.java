package com.ll.commars.domain.auth.dto;

public record KakaoMemberInfoDto(
	String email
) implements OAuthMemberInfoDto {

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getNickname() {
		return "";
	}
}
