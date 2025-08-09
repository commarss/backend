package com.ll.commars.domain.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoClientProperties(
		String tokenUrl,
		String userInfoUrl,
		String redirectUrl,
		String clientId
) {
}
