package com.ll.commars.domain.auth.client;

import static com.ll.commars.global.exception.ErrorCode.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.ll.commars.domain.auth.dto.KakaoMemberInfoDto;
import com.ll.commars.domain.auth.dto.KakaoTokenResponse;
import com.ll.commars.domain.auth.dto.OAuthMemberInfoDto;
import com.ll.commars.domain.auth.properties.KakaoClientProperties;
import com.ll.commars.domain.member.entity.AuthType;
import com.ll.commars.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoClient implements OAuthClient {

	private final KakaoClientProperties kakaoClientProperties;
	private final WebClient webClient;

	@Override
	public AuthType getProviderType() {
		return AuthType.KAKAO;
	}

	@Override
	public OAuthMemberInfoDto getUserInfo(String authorizationCode) {
		String accessToken = requestAccessToken(authorizationCode);

		return requestMemberInfo(accessToken);
	}

	private String requestAccessToken(String authorizationCode) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", "authorization_code");
		formData.add("client_id", kakaoClientProperties.clientId());
		formData.add("redirect_uri", kakaoClientProperties.redirectUrl());
		formData.add("code", authorizationCode);

		KakaoTokenResponse response = webClient.post()
			.uri(kakaoClientProperties.tokenUrl())
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.bodyValue(formData)
			.retrieve()
			.bodyToMono(KakaoTokenResponse.class)
			.block();

		if (response == null || response.accessToken() == null) {
			throw new CustomException(KAKAO_OAUTH_AUTHORIZATION_FAILED);
		}

		return response.accessToken();
	}

	private OAuthMemberInfoDto requestMemberInfo(String accessToken) {
		KakaoMemberInfoDto response = webClient.get()
			.uri(kakaoClientProperties.userInfoUrl())
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.retrieve()
			.bodyToMono(KakaoMemberInfoDto.class)
			.block();

		if (response == null || response.kakaoAccount() == null) {
			throw new CustomException(KAKAO_OAUTH_USER_INFO_FAILED);
		}

		return response;
	}
}
