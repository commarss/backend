package com.ll.commars.domain.auth.client;

import org.springframework.stereotype.Component;

import com.ll.commars.domain.auth.dto.OAuthMemberInfoDto;
import com.ll.commars.domain.member.entity.AuthType;

@Component
public class NaverClient implements OAuthClient {

	@Override
	public AuthType getProviderType() {
		return AuthType.NAVER;
	}

	@Override
	public OAuthMemberInfoDto getUserInfo(String accessToken) {
		return null;
	}
}
