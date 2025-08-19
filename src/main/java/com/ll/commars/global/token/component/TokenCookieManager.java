package com.ll.commars.global.token.component;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.ll.commars.global.token.JwtProperties;

import lombok.RequiredArgsConstructor;

// 관심사: RefreshToken을 담을 쿠키 생성에 관련한 모든 로직을 담당
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
			.maxAge(jwtProperties.refreshTokenExpiration())
			.build();
	}

	public ResponseCookie createExpiredCookie() {
		return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
			.maxAge(0)
			.path("/")
			.build();
	}
}
