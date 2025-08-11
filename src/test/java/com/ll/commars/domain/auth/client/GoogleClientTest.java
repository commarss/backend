package com.ll.commars.domain.auth.client;

import static com.ll.commars.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.ll.commars.domain.auth.dto.OAuthMemberInfoDto;
import com.ll.commars.global.annotation.IntegrationTest;
import com.ll.commars.global.exception.CustomException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@IntegrationTest
@DisplayName("GoogleClient 테스트")
public class GoogleClientTest {

	@Autowired
	private GoogleClient googleClient;

	private static MockWebServer mockWebServer;

	@BeforeAll
	static void setUp() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();
	}

	@AfterAll
	static void tearDown() throws IOException {
		mockWebServer.shutdown();
	}

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("oauth.google.token-url", () -> mockWebServer.url("/").toString());
		registry.add("oauth.google.user-info-url", () -> mockWebServer.url("/").toString());
	}

	@Test
	void authorization_code로_사용자_정보를_성공적으로_가져온다() throws InterruptedException {
		// given
		String tokenResponseJson = """
			{ "access_token": "google-test-access-token" }
			""";

		String userInfoResponseJson = """
			{
			    "sub": "google-user-id-12345",
			    "name": "구글유저",
			    "email": "test.user@google.com",
			    "email_verified": true
			}
			""";

		mockWebServer.enqueue(new MockResponse()
			.setBody(tokenResponseJson)
			.addHeader("Content-Type", "application/json"));

		mockWebServer.enqueue(new MockResponse()
			.setBody(userInfoResponseJson)
			.addHeader("Content-Type", "application/json"));

		// when
		OAuthMemberInfoDto memberInfo = googleClient.getUserInfo("google-test-authorization-code");

		// then
		assertThat(memberInfo).isNotNull();
		assertThat(memberInfo.getEmail()).isEqualTo("test.user@google.com");
		assertThat(memberInfo.getNickname()).isEqualTo("구글유저");

		// 토큰 요청 검증
		RecordedRequest tokenRequest = mockWebServer.takeRequest();
		assertThat(tokenRequest.getMethod()).isEqualTo("POST");
		assertThat(tokenRequest.getHeader("Content-Type")).startsWith("application/x-www-form-urlencoded");
		assertThat(tokenRequest.getBody().readUtf8()).contains("grant_type=authorization_code",
			"code=google-test-authorization-code");

		// 사용자 정보 요청 검증
		RecordedRequest userInfoRequest = mockWebServer.takeRequest();
		assertThat(userInfoRequest.getMethod()).isEqualTo("GET");
		assertThat(userInfoRequest.getHeader("Authorization")).isEqualTo("Bearer google-test-access-token");
	}

	@Test
	void 토큰_발급에_실패하면_예외가_발생한다() throws InterruptedException {
		String errorResponseJson = """
			{ "error": "invalid_grant", "error_description": "Bad Request" }
			""";

		mockWebServer.enqueue(new MockResponse()
			.setResponseCode(400)
			.setBody(errorResponseJson)
			.addHeader("Content-Type", "application/json"));

		// when & then
		assertThatThrownBy(() -> googleClient.getUserInfo("invalid-google-code"))
			.isInstanceOf(CustomException.class)
			.hasMessage(GOOGLE_OAUTH_AUTHORIZATION_FAILED.getMessage());

		// then
		RecordedRequest recordedRequest = mockWebServer.takeRequest();
		assertThat(recordedRequest.getMethod()).isEqualTo("POST");
		assertThat(recordedRequest.getBody().readUtf8()).contains("code=invalid-google-code");
	}

	@Test
	void 사용자_정보_조회에_실패하면_예외가_발생한다() throws InterruptedException {
		String tokenResponseJson = """
			{ "access_token": "google-test-access-token" }
			""";

		String errorResponseJson = """
			{ "error": { "code": 401, "message": "Request is missing required authentication credential." } }
			""";

		mockWebServer.enqueue(new MockResponse()
			.setBody(tokenResponseJson)
			.addHeader("Content-Type", "application/json"));

		mockWebServer.enqueue(new MockResponse()
			.setResponseCode(401)
			.setBody(errorResponseJson)
			.addHeader("Content-Type", "application/json"));

		// when & then
		assertThatThrownBy(() -> googleClient.getUserInfo("any-valid-google-code"))
			.isInstanceOf(CustomException.class)
			.hasMessage(GOOGLE_OAUTH_USER_INFO_FAILED.getMessage());

		// then
		RecordedRequest tokenRequest = mockWebServer.takeRequest();
		assertThat(tokenRequest.getMethod()).isEqualTo("POST");

		RecordedRequest userInfoRequest = mockWebServer.takeRequest();
		assertThat(userInfoRequest.getMethod()).isEqualTo("GET");
		assertThat(userInfoRequest.getHeader("Authorization")).isEqualTo("Bearer google-test-access-token");
	}
}
