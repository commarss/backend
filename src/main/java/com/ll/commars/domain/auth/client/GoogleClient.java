package com.ll.commars.domain.auth.client;

import static com.ll.commars.global.exception.ErrorCode.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.ll.commars.domain.auth.dto.GoogleMemberInfoDto;
import com.ll.commars.domain.auth.dto.GoogleTokenResponse;
import com.ll.commars.domain.auth.dto.OAuthMemberInfoDto;
import com.ll.commars.domain.auth.properties.GoogleClientProperties;
import com.ll.commars.domain.member.entity.AuthType;
import com.ll.commars.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GoogleClient implements OAuthClient {

	private final GoogleClientProperties googleClientProperties;
	private final WebClient webClient;

	@Override
	public AuthType getProviderType() {
		return AuthType.GOOGLE;
	}

	@Override
	public OAuthMemberInfoDto getUserInfo(String authorizationCode) {
		String accessToken = requestAccessToken(authorizationCode);

		return requestMemberInfo(accessToken);
	}

	private String requestAccessToken(String authorizationCode) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", "authorization_code");
		formData.add("client_id", googleClientProperties.clientId());
		formData.add("client_secret", googleClientProperties.clientSecret());
		formData.add("redirect_uri", googleClientProperties.redirectUrl());
		formData.add("code", authorizationCode);

		GoogleTokenResponse response = webClient.post()
			.uri(googleClientProperties.tokenUrl())
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.bodyValue(formData)
			.retrieve()
			.bodyToMono(GoogleTokenResponse.class)
			.block();

		if (response == null || response.accessToken() == null) {
			throw new CustomException(GOOGLE_OAUTH_AUTHORIZATION_FAILED);
		}

		return response.accessToken();
	}

	private OAuthMemberInfoDto requestMemberInfo(String accessToken) {
		GoogleMemberInfoDto response = webClient.get()
			.uri(googleClientProperties.userInfoUrl())
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.retrieve()
			.bodyToMono(GoogleMemberInfoDto.class)
			.block();

		if (response == null) {
			throw new CustomException(GOOGLE_OAUTH_USER_INFO_FAILED);
		}

		return response;
	}
}
