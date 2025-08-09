package com.ll.commars.domain.auth.config;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ll.commars.domain.auth.client.OAuthClient;
import com.ll.commars.domain.member.entity.AuthType;

@Configuration
public class OAuthClientConfig {

	@Bean
	public Map<AuthType, OAuthClient> oAuthClients(List<OAuthClient> clients) {
		return clients.stream().collect(
			Collectors.toUnmodifiableMap(OAuthClient::getProviderType, Function.identity())
		);
	}
}
