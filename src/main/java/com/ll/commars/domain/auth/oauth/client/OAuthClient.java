package com.ll.commars.domain.auth.oauth.client;

import com.ll.commars.domain.auth.dto.OAuthMemberInfoDto;
import com.ll.commars.domain.member.entity.ProviderType;

public interface OAuthClient {

	ProviderType getProviderType();

	String getAccessToken(String authorizationCode);

	OAuthMemberInfoDto getUserInfo(String accessToken);
}
