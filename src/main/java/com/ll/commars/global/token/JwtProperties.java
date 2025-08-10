package com.ll.commars.global.token;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String key, long accessTokenExpiration, long refreshTokenExpiration) {
}
