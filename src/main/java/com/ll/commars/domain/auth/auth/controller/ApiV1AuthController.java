package com.ll.commars.domain.auth.auth.controller;

import com.ll.commars.domain.auth.auth.entity.LoginRequest;
import com.ll.commars.global.jwt.component.JwtProvider;
import com.ll.commars.global.jwt.entity.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiV1AuthController {
    private final JwtProvider jwtProvider;

    @PostMapping("/login/jwt")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String accessToken = jwtProvider.generateAccessToken(1l, "email", "name", "profileImageUrl");

        System.out.println("accessToken = " + accessToken);

        String refreshToken = jwtProvider.generateRefreshToken(1l, "email", "name", "profileImageUrl");
        System.out.println("refreshToken = " + refreshToken);

        System.out.println("validateToken = " + jwtProvider.validateToken(accessToken));

        JwtToken jwtToken = jwtProvider.getJwtToken(accessToken);
        System.out.println("jwtToken = " + jwtToken);

        // 사용자 인증 로직 (예: 이메일과 비밀번호 검증)
//            String jwtToken = jwtTokenProvider.createToken(loginRequest.getEmail(), loginRequest.getName(), loginRequest.getProfileImageUrl());

//            // 쿠키 생성 및 설정
//            ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
//                    .httpOnly(true)   // XSS 공격 방지
////                    .secure(true)     // HTTPS 연결에서만 전송 (배포 시 필수)
//                    .path("/")        // 모든 경로에서 쿠키 사용 가능
//                    .maxAge(60 * 60)  // 1시간 동안 유지
//                    .sameSite("Strict")  // CSRF 방지
//                    .build();
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
//                    .body("로그인 성공");
//                return ResponseEntity.badRequest().body("로그인 실패");

    return ResponseEntity.ok().body(Map.of("accessToken", accessToken, "refresh", refreshToken, "jwtToken", jwtToken));

    }
}
