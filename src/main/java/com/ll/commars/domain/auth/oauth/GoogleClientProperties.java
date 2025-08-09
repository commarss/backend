package com.ll.commars.domain.auth.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.google")
public record GoogleClientProperties(
	String tokenUrl,
	String userInfoUrl,
	String redirectUrl,
	String clientId
) {
}
