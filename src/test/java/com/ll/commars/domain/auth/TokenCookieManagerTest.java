package com.ll.commars.domain.auth;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseCookie;

import com.ll.commars.domain.auth.token.JwtProperties;
import com.ll.commars.domain.auth.token.component.TokenCookieManager;
import com.ll.commars.global.annotation.UnitTest;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

@UnitTest
@DisplayName("TokenCookieManager 테스트")
class TokenCookieManagerTest {

	private TokenCookieManager tokenCookieManager;

	@Mock
	private JwtProperties jwtProperties;

	private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.build();

	private static final long MOCK_REFRESH_TOKEN_EXPIRATION_MS = Duration.ofDays(7).toMillis();

	@BeforeEach
	void setUp() {
		tokenCookieManager = new TokenCookieManager(jwtProperties);
	}

	@Nested
	class RefreshToken_쿠키_생성_테스트 {

		@Test
		void RefreshToken_쿠키를_생성한다() {
			// given
			when(jwtProperties.refreshTokenExpiration()).thenReturn(MOCK_REFRESH_TOKEN_EXPIRATION_MS);

			// 토큰에 사용될 수 있는 문자열은 US-ASCII 문자로 제한된다.
			String randomString = fixtureMonkey.giveMeOne(String.class);
			String refreshToken = Base64.getUrlEncoder()
				.withoutPadding()
				.encodeToString(randomString.getBytes(StandardCharsets.UTF_8));

			long expectedMaxAgeInSeconds = Duration.ofDays(7).getSeconds();

			// when
			ResponseCookie cookie = tokenCookieManager.createRefreshTokenCookie(refreshToken);

			// then
			assertAll(
				() -> assertThat(cookie.getName()).isEqualTo(TokenCookieManager.REFRESH_TOKEN_COOKIE_NAME),
				() -> assertThat(cookie.getValue()).isEqualTo(refreshToken),
				() -> assertThat(cookie.isHttpOnly()).isTrue(),
				() -> assertThat(cookie.isSecure()).isTrue(),
				() -> assertThat(cookie.getSameSite()).isEqualTo("Strict"),
				() -> assertThat(cookie.getPath()).isEqualTo("/"),
				() -> assertThat(cookie.getMaxAge().getSeconds()).isEqualTo(expectedMaxAgeInSeconds)
			);
		}
	}

	@Nested
	class 만료_쿠키_생성_테스트 {

		@Test
		void 만료_시간이_0인_쿠키를_생성한다() {
			// when
			ResponseCookie cookie = tokenCookieManager.createExpiredCookie();

			// then
			assertAll(
				() -> assertThat(cookie.getName()).isEqualTo(TokenCookieManager.REFRESH_TOKEN_COOKIE_NAME),
				() -> assertThat(cookie.getValue()).isEmpty(),
				() -> assertThat(cookie.getPath()).isEqualTo("/"),
				() -> assertThat(cookie.getMaxAge().getSeconds()).isZero()
			);
		}
	}
}
