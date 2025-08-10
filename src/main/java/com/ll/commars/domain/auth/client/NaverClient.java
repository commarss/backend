package com.ll.commars.domain.auth.client;

import static com.ll.commars.global.exception.ErrorCode.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.ll.commars.domain.auth.dto.NaverMemberInfoDto;
import com.ll.commars.domain.auth.dto.NaverTokenResponse;
import com.ll.commars.domain.auth.dto.OAuthMemberInfoDto;
import com.ll.commars.domain.auth.properties.NaverClientProperties;
import com.ll.commars.domain.member.entity.AuthType;
import com.ll.commars.global.exception.CustomException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class NaverClient implements OAuthClient {

	private final NaverClientProperties naverClientProperties;
	private final WebClient webClient;

	@Override
	public AuthType getProviderType() {
		return AuthType.NAVER;
	}

	@Override
	public OAuthMemberInfoDto getUserInfo(String authorizationCode) {
		String accessToken = requestAccessToken(authorizationCode);
		return requestMemberInfo(accessToken);
	}

	private String requestAccessToken(String authorizationCode) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", "authorization_code");
		formData.add("client_id", naverClientProperties.clientId());
		formData.add("client_secret", naverClientProperties.clientSecret());
		formData.add("code", authorizationCode);

		NaverTokenResponse response = webClient.post()
			.uri(naverClientProperties.tokenUrl())
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.bodyValue(formData)
			.retrieve()
			.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
				clientResponse -> clientResponse.bodyToMono(String.class)
					.flatMap(errorBody -> Mono.error(new CustomException(NAVER_OAUTH_AUTHORIZATION_FAILED)))
			)
			.bodyToMono(NaverTokenResponse.class)
			.block();

		if (response == null || response.accessToken() == null) {
			throw new CustomException(NAVER_OAUTH_AUTHORIZATION_FAILED);
		}

		return response.accessToken();
	}

	private OAuthMemberInfoDto requestMemberInfo(String accessToken) {
		NaverMemberInfoDto response = webClient.get()
			.uri(naverClientProperties.userInfoUrl())
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.retrieve()
			.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
				clientResponse -> clientResponse.bodyToMono(String.class)
					.flatMap(errorBody -> Mono.error(new CustomException(NAVER_OAUTH_USER_INFO_FAILED)))
			)
			.bodyToMono(NaverMemberInfoDto.class)
			.block();

		if (response == null || response.response() == null) {
			throw new CustomException(NAVER_OAUTH_USER_INFO_FAILED);
		}

		return response;
	}
}
