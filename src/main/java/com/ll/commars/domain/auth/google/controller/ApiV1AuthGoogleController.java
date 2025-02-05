package com.ll.commars.domain.auth.google.controller;

import com.ll.commars.domain.auth.google.service.GoogleService;
import com.ll.commars.domain.member.member.entity.Member;
import com.ll.commars.global.jwt.component.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ApiV1AuthGoogleController {
    private final JwtProvider jwtProvider;
    private final GoogleService googleService;

    @PostMapping("/login/google")
    public ResponseEntity<?> verifyGoogleIdToken(@RequestBody Map<String, String> body) {
        String idToken = body.get("idToken");
        System.out.println("idToken = " + idToken);

        Optional<Member> googleAuth = googleService.loginForGoogle(idToken);

        if (googleAuth.isEmpty()) {
            return ResponseEntity.badRequest().body("Google ID Token 검증 실패");
        }

        String accessToken = jwtProvider.generateAccessToken(googleAuth.get());
        String refreshToken = jwtProvider.generateRefreshToken(googleAuth.get());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtProvider.REFRESH_TOKEN_VALIDITY)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .body(Map.of( "googleUser", googleAuth.get(), "accessToken", accessToken));

    }

}
