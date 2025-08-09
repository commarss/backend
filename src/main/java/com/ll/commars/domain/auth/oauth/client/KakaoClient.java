package com.ll.commars.domain.auth.oauth.client;

import com.ll.commars.domain.auth.dto.OAuthResponse;
import com.ll.commars.domain.member.entity.ProviderType;

public class KakaoClient implements OAuthClient {

	@Override
	public ProviderType getProviderType() {
		return ProviderType.KAKAO;
	}

	@Override
	public String getAccessToken(String authorizationCode) {
		return null;
	}

	@Override
	public OAuthResponse getUserInfo(String accessToken) {
		return null;
	}
}
