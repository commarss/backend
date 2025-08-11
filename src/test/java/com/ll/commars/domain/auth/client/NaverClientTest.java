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
@DisplayName("NaverClient 테스트")
public class NaverClientTest {

	@Autowired
	private NaverClient naverClient;

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
		registry.add("oauth.naver.token-url", () -> mockWebServer.url("/").toString());
		registry.add("oauth.naver.user-info-url", () -> mockWebServer.url("/").toString());
	}

	@Test
	void authorization_code로_사용자_정보를_성공적으로_가져온다() throws InterruptedException {
		// given
		String tokenResponseJson = """
            { "access_token": "naver-test-access-token" }
            """;

		String userInfoResponseJson = """
            {
              "resultcode": "00",
              "message": "success",
              "response": {
                "id": "naver-user-id-123",
                "nickname": "네이버유저",
                "email": "test.user@naver.com"
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
		OAuthMemberInfoDto memberInfo = naverClient.getUserInfo("naver-test-authorization-code");

		// then
		assertThat(memberInfo).isNotNull();
		assertThat(memberInfo.getEmail()).isEqualTo("test.user@naver.com");
		assertThat(memberInfo.getNickname()).isEqualTo("네이버유저");

		// 토큰 요청 검증
		RecordedRequest tokenRequest = mockWebServer.takeRequest();
		assertThat(tokenRequest.getMethod()).isEqualTo("POST");
		assertThat(tokenRequest.getHeader("Content-Type")).startsWith("application/x-www-form-urlencoded");
		assertThat(tokenRequest.getBody().readUtf8()).contains("grant_type=authorization_code", "code=naver-test-authorization-code");

		// 사용자 정보 요청 검증
		RecordedRequest userInfoRequest = mockWebServer.takeRequest();
		assertThat(userInfoRequest.getMethod()).isEqualTo("GET");
		assertThat(userInfoRequest.getHeader("Authorization")).isEqualTo("Bearer naver-test-access-token");
	}

	@Test
	void 토큰_발급에_실패하면_예외가_발생한다() throws InterruptedException {
		// given
		String errorResponseJson = """
            { "error": "invalid_client", "error_description": "유효하지 않은 클라이언트 정보입니다." }
            """;

		mockWebServer.enqueue(new MockResponse()
			.setResponseCode(401)
			.setBody(errorResponseJson)
			.addHeader("Content-Type", "application/json"));

		// when & then
		assertThatThrownBy(() -> naverClient.getUserInfo("invalid-naver-code"))
			.isInstanceOf(CustomException.class)
			.hasMessage(NAVER_OAUTH_AUTHORIZATION_FAILED.getMessage());

		// then
		RecordedRequest recordedRequest = mockWebServer.takeRequest();
		assertThat(recordedRequest.getMethod()).isEqualTo("POST");
		assertThat(recordedRequest.getBody().readUtf8()).contains("code=invalid-naver-code");
	}

	@Test
	void 사용자_정보_조회에_실패하면_예외가_발생한다() throws InterruptedException {
		// given
		String tokenResponseJson = """
         { "access_token": "naver-test-access-token" }
         """;

		String errorResponseJson = """
         { "resultcode": "024", "message": "Authentication failed (인증에 실패했습니다.)" }
         """;

		mockWebServer.enqueue(new MockResponse()
			.setBody(tokenResponseJson)
			.addHeader("Content-Type", "application/json"));

		mockWebServer.enqueue(new MockResponse()
			.setResponseCode(401)
			.setBody(errorResponseJson)
			.addHeader("Content-Type", "application/json"));

		// when & then
		assertThatThrownBy(() -> naverClient.getUserInfo("any-valid-naver-code"))
			.isInstanceOf(CustomException.class)
			.hasMessage(NAVER_OAUTH_USER_INFO_FAILED.getMessage());

		// then
		RecordedRequest tokenRequest = mockWebServer.takeRequest();
		assertThat(tokenRequest.getMethod()).isEqualTo("POST");

		RecordedRequest userInfoRequest = mockWebServer.takeRequest();
		assertThat(userInfoRequest.getMethod()).isEqualTo("GET");
		assertThat(userInfoRequest.getHeader("Authorization")).isEqualTo("Bearer naver-test-access-token");
	}
}
