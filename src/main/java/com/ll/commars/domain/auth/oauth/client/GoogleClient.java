package com.ll.commars.domain.auth.oauth.client;

import org.springframework.stereotype.Component;

import com.ll.commars.domain.auth.dto.OAuthMemberInfoDto;
import com.ll.commars.domain.member.entity.ProviderType;

@Component
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
	public OAuthMemberInfoDto getUserInfo(String accessToken) {
		return null;
	}
}
