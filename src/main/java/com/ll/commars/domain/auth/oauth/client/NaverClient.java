package com.ll.commars.domain.auth.oauth.client;

import org.springframework.stereotype.Component;

import com.ll.commars.domain.auth.dto.OAuthMemberInfoDto;
import com.ll.commars.domain.member.entity.ProviderType;

@Component
public class NaverClient implements OAuthClient {

	@Override
	public ProviderType getProviderType() {
		return ProviderType.NAVER;
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
