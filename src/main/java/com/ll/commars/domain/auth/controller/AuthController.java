package com.ll.commars.domain.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.auth.dto.SignInRequest;
import com.ll.commars.domain.auth.dto.SignInResponse;
import com.ll.commars.domain.auth.dto.SignUpRequest;
import com.ll.commars.domain.auth.dto.SignUpResponse;
import com.ll.commars.domain.auth.dto.TokenReissueResponse;
import com.ll.commars.domain.auth.service.AuthService;
import com.ll.commars.domain.auth.service.SignUpService;
import com.ll.commars.domain.auth.token.component.TokenCookieManager;
import com.ll.commars.domain.auth.token.entity.TokenValue;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final SignUpService signUpService;
	private final AuthService authService;
	private final TokenCookieManager tokenCookieManager;

	@PostMapping("/sign-up")
	public ResponseEntity<SignUpResponse> signUp(
		@Valid @RequestBody SignUpRequest request
	) {
		SignUpResponse response = signUpService.signUp(request);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/sign-in")
	public ResponseEntity<SignInResponse> signIn(
		@Valid @RequestBody SignInRequest request
	) {
		SignInResponse response = authService.signIn(request);

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
		@CookieValue(TokenCookieManager.REFRESH_TOKEN_COOKIE_NAME) String refreshTokenValue
	) {
		TokenReissueResponse response = authService.reissueToken(refreshTokenValue);

		ResponseCookie newRefreshTokenCookie = tokenCookieManager
			.createRefreshTokenCookie(response.refreshToken());

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString())
			.body(response);
	}

	// @PostMapping("/login/google")
	// public ResponseEntity<?> verifyGoogleIdToken(@RequestBody Map<String, String> body) {
	// 	String idToken = body.get("idToken");
	// 	System.out.println("idToken = " + idToken);
	//
	// 	Optional<User> googleAuth = googleService.loginForGoogle(idToken);
	//
	// 	if (googleAuth.isEmpty()) {
	// 		return ResponseEntity.badRequest().body("Google ID Token 검증 실패");
	// 	}
	//
	// 	return authService.login(googleAuth.get());
	// }

	// @PostMapping("/login/kakao")
	// public ResponseEntity<?> loginForKakao(@RequestBody Map<String, String> body, HttpSession session) {
	// 	System.out.println("Request body = " + body);
	// 	String code = body.get("code");
	// 	System.out.println("Received code = " + code);
	//
	// 	// 세션에서 이미 사용한 인가코드인지 확인하여 중복 사용 방지
	// 	if (session.getAttribute("usedKakaoCode") != null && session.getAttribute("usedKakaoCode").equals(code)) {
	// 		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	// 			.body("이미 사용된 인가코드입니다.");
	// 	}
	// 	// 현재 인가코드를 세션에 저장
	// 	session.setAttribute("usedKakaoCode", code);
	//
	// 	// 인가 코드를 통해 access token 발급
	// 	String kakaoAccessToken = kakaoService.getAccessToken(code);
	// 	System.out.println("Kakao accessToken = " + kakaoAccessToken);
	//
	// 	if (kakaoAccessToken == null) {
	// 		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	// 			.body("토큰 발급 실패");
	// 	}
	//
	// 	// access token을 이용해 사용자 프로필 조회
	// 	Map<String, Object> userProfile = kakaoService.getUserProfile(kakaoAccessToken);
	// 	System.out.println("Kakao userProfile = " + userProfile);
	//
	// 	// 사용자 프로필 정보를 기반으로 User 엔티티 생성 및 로그인
	// 	User kakaoUser = kakaoService.loginForKakao(userProfile);
	// 	return authService.login(kakaoUser);
	// }
	//
	// @PostMapping("/login/naver")
	// public ResponseEntity<?> loginForNaver(@RequestBody Map<String, String> body) {
	//
	// 	System.out.println("body = " + body);
	// 	String naverAccessToken = naverservice.getAccessToken(body.get("code"), body.get("state"));
	// 	System.out.println("accessToken = " + naverAccessToken);
	//
	// 	if (naverAccessToken == null) {
	// 		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 발급 실패");
	// 	}
	//
	// 	Map<String, Object> userProfile = naverservice.getUserProfile(naverAccessToken);
	//
	// 	System.out.println("naver userProfile = " + userProfile);
	//
	// 	User naverUser = naverservice.loginForNaver(userProfile);
	//
	// 	return authService.login(naverUser);
	// }
	//
	// @GetMapping("/login/naver/state")
	// public ResponseEntity<?> naverState() {
	// 	System.out.println("get state = " + state);
	// 	return ResponseEntity.ok(state);
	// }
	//
	// @GetMapping("/logout/naver")
	// public void logout(HttpServletResponse response) {
	// 	String accessToken = response.getHeader(HttpHeaders.AUTHORIZATION);
	//
	// 	ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
	// 		.httpOnly(true)
	// 		.secure(true)
	// 		.path("/")
	// 		.maxAge(0)
	// 		.sameSite("Strict")
	// 		.build();
	//
	// 	response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
	// 	try {
	// 		response.sendRedirect("/");
	// 	} catch (IOException e) {
	// 		e.printStackTrace();
	// 	}
	// }
	//
	//
}
