package com.ll.commars.domain.auth.google.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.auth.auth.service.AuthService;
import com.ll.commars.domain.auth.google.service.GoogleService;
import com.ll.commars.domain.user.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ApiV1AuthGoogleController {

	private final GoogleService googleService;
	private final AuthService authService;

	@PostMapping("/login/google")
	public ResponseEntity<?> verifyGoogleIdToken(@RequestBody Map<String, String> body) {
		String idToken = body.get("idToken");
		System.out.println("idToken = " + idToken);

		Optional<User> googleAuth = googleService.loginForGoogle(idToken);

		if (googleAuth.isEmpty()) {
			return ResponseEntity.badRequest().body("Google ID Token 검증 실패");
		}

		return authService.login(googleAuth.get());
	}

}
