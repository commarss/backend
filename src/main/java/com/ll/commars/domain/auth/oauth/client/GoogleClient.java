package com.ll.commars.domain.auth.oauth.client;

import com.ll.commars.domain.auth.dto.OAuthResponse;
import com.ll.commars.domain.member.entity.ProviderType;

public class GoogleClient implements OAuthClient {

	@Override
	public ProviderType getProviderType() {
		return ProviderType.GOOGLE;
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
