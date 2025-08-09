package com.ll.commars.domain.auth.oauth.client;

import com.ll.commars.domain.auth.dto.OAuthResponse;
import com.ll.commars.domain.member.entity.ProviderType;

public interface OAuthClient {

	ProviderType getProviderType();

	String getAccessToken(String authorizationCode);

	OAuthResponse getUserInfo(String accessToken);
}
