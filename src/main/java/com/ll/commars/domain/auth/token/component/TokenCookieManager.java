package com.ll.commars.domain.auth.token.component;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.ll.commars.domain.auth.token.JwtProperties;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenCookieManager {

	private final JwtProperties jwtProperties;

	public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

	public ResponseCookie createRefreshTokenCookie(String refreshToken) {
		return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
			.httpOnly(true)
			.secure(true)
			.sameSite("Strict")
			.path("/")
			.maxAge(Duration.ofMillis(jwtProperties.refreshTokenExpiration()).getSeconds())
			.build();
	}

	public ResponseCookie createExpiredCookie() {
		return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
			.maxAge(0)
			.path("/")
			.build();
	}
}
