package com.ll.commars.domain.auth.token;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String key, long accessTokenExpiration, long refreshTokenExpiration) {
}
