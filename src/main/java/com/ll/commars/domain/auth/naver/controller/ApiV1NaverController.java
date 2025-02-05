package com.ll.commars.domain.auth.naver.controller;


import com.ll.commars.domain.auth.naver.service.NaverService;
import com.ll.commars.domain.member.member.entity.Member;
import com.ll.commars.global.jwt.component.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiV1NaverController {
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String redirectUri;

    private final String state = UUID.randomUUID().toString();

    private final JwtProvider jwtProvider;
    private final NaverService naverservice;



    @PostMapping("/login/naver")
    public ResponseEntity<?> loginForNaver(@RequestBody Map<String, String> body) {

        System.out.println("body = " + body);
        String naverAccessToken = naverservice.getAccessToken(body.get("code"), body.get("state"));
        System.out.println("accessToken = " + naverAccessToken);

        if (naverAccessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 발급 실패");
        }

        Map<String, Object> userProfile = naverservice.getUserProfile(naverAccessToken);

        System.out.println("naver userProfile = " + userProfile);

//        Member naverMember = naverservice.loginForNaver(userProfile);

//        String accessToken = jwtProvider.generateAccessToken(naverMember.getId());
//        String refreshToken = jwtProvider.generateRefreshToken(naverMember.getId());

        Long id = 2L;

        Optional<Member> naverMember = Optional.ofNullable(Member.builder()
                .id(id)
                .email((String) userProfile.get("email"))
                .name((String) userProfile.get("name"))
                .profile((String) userProfile.get("profile_image"))
                .build());

        String accessToken = jwtProvider.generateAccessToken(naverMember.get());
        String refreshToken = jwtProvider.generateRefreshToken(naverMember.get());

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
                .body(Map.of("naverUser", naverMember.get(), "accessToken", accessToken));
    }

//    @GetMapping("/login/naver/callback")
//    public ResponseEntity<?> naverCallback(@RequestParam String code, @RequestParam String state) {
//        System.out.println("code = " + code);
//        System.out.println("state = " + state);
//        String naverAccessToken = naverservice.getAccessToken(code, state);
//
//        if (naverAccessToken == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 발급 실패");
//        }
//
//        Map<String, Object> profile = naverservice.getUserProfile(naverAccessToken);
//
//        Map<String, String> userProfile = (Map<String, String>) naverservice.getUserProfile(naverAccessToken).get("response");
//
//        System.out.println("profile = " + profile);
//        System.out.println("naver userProfile = " + userProfile);
//
////        Optional<Member> naverMember = naverservice.loginForNaver(userProfile);
////
////        String accessToken = jwtProvider.generateAccessToken(naverMember.get().getId());
////        String refreshToken = jwtProvider.generateRefreshToken(naverMember.get().getId());
//
////        String id = (String) userProfile.get("id");
//
//        String id = userProfile.get("response");
//        System.out.println("id = " + id);
//
//        String accessToken = jwtProvider.generateAccessToken(Long.parseLong(id));
//        String refreshToken = jwtProvider.generateRefreshToken(Long.parseLong(id));
//
//        System.out.println("accessToken = " + accessToken);
//
//        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
//                .httpOnly(true)
//                .secure(true)
//                .path("/")
//                .maxAge(jwtProvider.REFRESH_TOKEN_VALIDITY)
//                .sameSite("Strict")
//                .build();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
//                .body(Map.of("naverMember", userProfile, "accessToken", accessToken));
//    }

    @GetMapping("/login/naver/state")
    public ResponseEntity<?> naverState() {
        System.out.println("get state = " + state);
        return ResponseEntity.ok(state);
    }

    @GetMapping("/logout/naver")
    public void logout(HttpServletResponse response) {
        String accessToken = response.getHeader(HttpHeaders.AUTHORIZATION);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
