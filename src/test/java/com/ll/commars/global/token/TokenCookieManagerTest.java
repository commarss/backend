package com.ll.commars.global.token;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseCookie;

import com.ll.commars.global.annotation.UnitTest;
import com.ll.commars.global.token.component.TokenCookieManager;

@UnitTest
@DisplayName("TokenCookieManager 테스트")
class TokenCookieManagerTest {

	@InjectMocks
	private TokenCookieManager tokenCookieManager;

	@Mock
	private JwtProperties jwtProperties;

	private static final long REFRESH_TOKEN_EXPIRATION = Duration.ofDays(7).getSeconds();

	@Nested
	class RefreshToken_쿠키_생성_테스트 {

		@Test
		void RefreshToken_쿠키를_생성한다() {
			// given
			when(jwtProperties.refreshTokenExpiration()).thenReturn(REFRESH_TOKEN_EXPIRATION);

			String refreshToken = UUID.randomUUID().toString();

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
