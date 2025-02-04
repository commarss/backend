package com.ll.commars.domain.auth.google.controller;

import com.ll.commars.domain.auth.google.entity.GoogleAuth;
import com.ll.commars.domain.auth.google.service.GoogleAuthService;
import com.ll.commars.global.jwt.component.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ApiV1AuthGoogleController {
    private final JwtProvider jwtProvider;
    private final GoogleAuthService googleAuthService;

    @PostMapping("/google")
    public ResponseEntity<?> verifyGoogleIdToken(@RequestBody Map<String, String> body) {
        String idToken = body.get("idToken");
        System.out.println("idToken = " + idToken);

        Optional<GoogleAuth> googleAuth = googleAuthService.loginForGoogle(idToken);

        if (googleAuth.isEmpty()) {
            return ResponseEntity.badRequest().body("Google ID Token 검증 실패");
        }

        String accessToken = jwtProvider.generateAccessToken(googleAuth.get().getId(), googleAuth.get().getEmail(), googleAuth.get().getName(), googleAuth.get().getProfileImageUrl());
        String refreshToken = jwtProvider.generateRefreshToken(googleAuth.get().getId(), googleAuth.get().getEmail(), googleAuth.get().getName(), googleAuth.get().getProfileImageUrl());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtProvider.REFRESH_TOKEN_VALIDITY)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header("Authorization", "Bearer " + accessToken)
                .body(Map.of( "googleAuth", googleAuth.get(), "accessToken", accessToken));

    }

}
