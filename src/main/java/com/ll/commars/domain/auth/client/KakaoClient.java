package com.ll.commars.domain.auth.client;

import org.springframework.stereotype.Component;

import com.ll.commars.domain.auth.dto.OAuthMemberInfoDto;
import com.ll.commars.domain.member.entity.AuthType;

@Component
public class KakaoClient implements OAuthClient {

	@Override
	public AuthType getProviderType() {
		return AuthType.KAKAO;
	}

	@Override
	public String getAccessToken(String authorizationCode) {
		return null;
	}

	@Override
	public OAuthMemberInfoDto getUserInfo(String accessToken) {
		return null;
	}
}
