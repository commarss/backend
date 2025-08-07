package com.ll.commars.domain.auth.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.auth.token.JwtProvider;
import com.ll.commars.domain.user.entity.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final JwtProvider jwtProvider;

	@PostMapping("/sign-up")

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
	// @PostMapping("/logout")
	// public ResponseEntity<?> logout(HttpServletRequest request) {
	// 	ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
	// 		.httpOnly(true)
	// 		.secure(true)
	// 		.path("/")
	// 		.maxAge(0)
	// 		.sameSite("Strict")
	// 		.build();
	//
	// 	return ResponseEntity.ok()
	// 		.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
	// 		.body(Map.of("server", "logout ok"));
	// }
	//
	// @GetMapping("/refresh")
	// public ResponseEntity<?> refresh(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
	//
	// 	String requestToken = extractRefreshTokenFromCookies(request);
	//
	// 	if (requestToken == null) {
	// 		return ResponseEntity.badRequest().body("Refresh Token이 존재하지 않습니다.");
	// 	}
	//
	// 	Optional<User> user = userService.findById(Long.parseLong(userDetails.getUsername()));
	//
	// 	if (user.isEmpty()) {
	// 		return ResponseEntity.badRequest().body("사용자 정보가 존재하지 않습니다.");
	// 	}
	//
	// 	String refreshToken = jwtProvider.generateRefreshToken(user.get());
	//
	// 	ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
	// 		.httpOnly(true)
	// 		.secure(true)
	// 		.path("/")
	// 		.maxAge(jwtProvider.REFRESH_TOKEN_VALIDITY)
	// 		.sameSite("Strict")
	// 		.build();
	//
	// 	return ResponseEntity.ok()
	// 		.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
	// 		.body(Map.of("server", "refresh ok"));
	// }
	//
	// private String extractRefreshTokenFromCookies(HttpServletRequest request) {
	// 	if (request.getCookies() != null) {
	// 		for (Cookie cookie : request.getCookies()) {
	// 			if (cookie.getName().equals("refreshToken")) {
	// 				return cookie.getValue();
	// 			}
	// 		}
	// 	}
	// 	return null;
	// }
}
