package com.ll.commars.domain.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.auth.dto.OAuthRequest;
import com.ll.commars.domain.auth.dto.OAuthResponse;
import com.ll.commars.domain.auth.dto.SignInRequest;
import com.ll.commars.domain.auth.dto.SignInResponse;
import com.ll.commars.domain.auth.dto.SignUpRequest;
import com.ll.commars.domain.auth.dto.SignUpResponse;
import com.ll.commars.domain.auth.dto.TokenReissueResponse;
import com.ll.commars.domain.auth.service.AuthService;
import com.ll.commars.domain.member.entity.AuthType;
import com.ll.commars.global.token.component.TokenCookieManager;
import com.ll.commars.global.token.entity.TokenValue;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final TokenCookieManager tokenCookieManager;

	@PostMapping("/sign-up")
	public ResponseEntity<SignUpResponse> signUp(
		@Valid @RequestBody SignUpRequest request
	) {
		SignUpResponse response = authService.emailSignUp(request);

		return ResponseEntity.ok(response);
	}

	@PostMapping("email/sign-in")
	public ResponseEntity<SignInResponse> signIn(
		@Valid @RequestBody SignInRequest request
	) {
		SignInResponse response = authService.emailSignIn(request);

		ResponseCookie refreshTokenCookie = tokenCookieManager
			.createRefreshTokenCookie(response.refreshToken());

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
			.body(response);
	}

	@PostMapping("sign-out")
	public ResponseEntity<Void> signOut(
		@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
	) {
		String tokenString = authHeader.substring("Bearer ".length());
		TokenValue accessTokenValue = TokenValue.of(tokenString);

		authService.signOut(accessTokenValue);

		ResponseCookie expiredCookie = tokenCookieManager.createExpiredCookie();

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
			.build();
	}

	@PostMapping("/reissue")
	public ResponseEntity<TokenReissueResponse> reissueToken(
		@CookieValue(TokenCookieManager.REFRESH_TOKEN_COOKIE_NAME) String refreshTokenValueString
	) {
		TokenReissueResponse response = authService.reissueToken(refreshTokenValueString);

		ResponseCookie newRefreshTokenCookie = tokenCookieManager
			.createRefreshTokenCookie(response.refreshToken());

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString())
			.body(response);
	}

	@DeleteMapping("/withdraw")
	public ResponseEntity<Void> withdraw(
		@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
	) {
		String tokenString = authHeader.substring("Bearer ".length());
		TokenValue accessTokenValue = TokenValue.of(tokenString);

		authService.withdraw(accessTokenValue);

		ResponseCookie expiredCookie = tokenCookieManager.createExpiredCookie();

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
			.build();
	}

	@PostMapping("/{provider}")
	public ResponseEntity<OAuthResponse> processOAuth(
		@PathVariable String provider,
		@Valid @RequestBody OAuthRequest request
	) {
		AuthType authType = AuthType.from(provider);
		OAuthResponse response = authService.processOAuth(authType, request);

		return ResponseEntity.ok(response);
	}
}
