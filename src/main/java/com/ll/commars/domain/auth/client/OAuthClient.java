package com.ll.commars.domain.auth.client;

import com.ll.commars.domain.auth.dto.OAuthMemberInfoDto;
import com.ll.commars.domain.member.entity.AuthType;

public interface OAuthClient {

	AuthType getProviderType();

	OAuthMemberInfoDto getUserInfo(String authorizationCode);
}
