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
@DisplayName("KakaoClient 테스트")
class KakaoClientTest {

	@Autowired
	private KakaoClient kakaoClient;

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
		registry.add("oauth.kakao.token-url", () -> mockWebServer.url("/").toString());
		registry.add("oauth.kakao.user-info-url", () -> mockWebServer.url("/").toString());
	}

	@Test
	void authorization_code로_사용자_정보를_성공적으로_가져온다() throws InterruptedException {
		// given
		String tokenResponseJson = """
			{
			    "token_type":"bearer",
			    "access_token":"test-access-token",
			    "expires_in":43199,
			    "refresh_token":"test-refresh-token",
			    "refresh_token_expires_in":5184000,
			    "scope":"account_email profile_nickname"
			}
			""";

		String userInfoResponseJson = """
			{
			    "id":123456789,
			    "kakao_account": {
			    	"email":"test@kakao.com",
			        "profile": {
			            "nickname": "테스트유저"
			        }
			    }
			}
			""";

		mockWebServer.enqueue(new MockResponse()
			.setBody(tokenResponseJson)
			.addHeader("Content-Type", "application/json"));

		mockWebServer.enqueue(new MockResponse()
			.setBody(userInfoResponseJson)
			.addHeader("Content-Type", "application/json"));

		// when
		OAuthMemberInfoDto memberInfo = kakaoClient.getUserInfo("test-authorization-code");

		// then
		assertThat(memberInfo).isNotNull();
		assertThat(memberInfo.getEmail()).isEqualTo("test@kakao.com");
		assertThat(memberInfo.getNickname()).isEqualTo("테스트유저");

		// 토큰 요청
		RecordedRequest tokenRequest = mockWebServer.takeRequest();
		assertThat(tokenRequest.getMethod()).isEqualTo("POST");
		assertThat(tokenRequest.getHeader("Content-Type")).startsWith("application/x-www-form-urlencoded");
		assertThat(tokenRequest.getBody().readUtf8()).contains("grant_type=authorization_code",
			"code=test-authorization-code");

		// 사용자 정보 요청
		RecordedRequest userInfoRequest = mockWebServer.takeRequest();
		assertThat(userInfoRequest.getMethod()).isEqualTo("GET");
		assertThat(userInfoRequest.getHeader("Authorization")).isEqualTo("Bearer test-access-token");
	}

	@Test
	void 토큰_발급에_실패하면_예외가_발생한다() throws InterruptedException {
		// given
		String errorResponseJson = """
			{"error":"invalid_grant","error_description":"authorization code not found for...","error_code":"KOE320"}
			""";

		mockWebServer.enqueue(new MockResponse()
			.setResponseCode(400)
			.setBody(errorResponseJson)
			.addHeader("Content-Type", "application/json"));

		// when & then
		assertThatThrownBy(() -> kakaoClient.getUserInfo("invalid-code"))
			.isInstanceOf(CustomException.class)
			.hasMessage(KAKAO_OAUTH_AUTHORIZATION_FAILED.getMessage());

		RecordedRequest recordedRequest = mockWebServer.takeRequest();
		assertThat(recordedRequest.getMethod()).isEqualTo("POST");
		assertThat(recordedRequest.getBody().readUtf8()).contains("code=invalid-code");
	}

	@Test
	void 사용자_정보_조회에_실패하면_예외가_발생한다() throws InterruptedException {
		// given
		String tokenResponseJson = """
			{ "access_token": "test-access-token" }
			""";

		String errorResponseJson = """
			{"msg":"user not found","code":-401}
			""";

		mockWebServer.enqueue(new MockResponse()
			.setBody(tokenResponseJson)
			.addHeader("Content-Type", "application/json"));

		mockWebServer.enqueue(new MockResponse()
			.setResponseCode(401)
			.setBody(errorResponseJson)
			.addHeader("Content-Type", "application/json"));

		// when & then
		assertThatThrownBy(() -> kakaoClient.getUserInfo("any-valid-code"))
			.isInstanceOf(CustomException.class)
			.hasMessage(KAKAO_OAUTH_USER_INFO_FAILED.getMessage());

		RecordedRequest tokenRequest = mockWebServer.takeRequest();
		assertThat(tokenRequest.getMethod()).isEqualTo("POST");

		RecordedRequest userInfoRequest = mockWebServer.takeRequest();
		assertThat(userInfoRequest.getMethod()).isEqualTo("GET");
		assertThat(userInfoRequest.getHeader("Authorization")).isEqualTo("Bearer test-access-token");
	}
}
