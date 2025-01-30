package com.ll.commars.domain.auth.google.controller;

import com.ll.commars.domain.auth.auth.service.JwtTokenProvider;
import com.ll.commars.domain.auth.google.entity.GoogleAuthResponse;
import com.ll.commars.domain.auth.google.entity.GoogleUser;
import com.ll.commars.domain.auth.google.service.GoogleOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ApiV1AuthGoogleController {
    private final GoogleOAuthService googleOAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/google")
    public ResponseEntity<?> verifyGoogleIdToken(@RequestBody Map<String, String> body) {
        String idToken = body.get("idToken");
        System.out.println("idToken = " + idToken);


        String verificationUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Google의 Token 검증 API 호출
            Map<String, Object> response = restTemplate.getForObject(verificationUrl, Map.class);

            // 1. 토큰이 유효하면 사용자 정보 가져오기
            String email = (String) response.get("email");
            String name = (String) response.get("name");
            String picture = (String) response.get("picture");

            // 2. audience(Client ID) 검증
            String clientId = (String) response.get("aud");
            if (!clientId.equals("360427694540-i4i2593i1u708vfhc15uts2hpri1r467.apps.googleusercontent.com")) {
                return ResponseEntity.badRequest().body("Invalid audience.");
            }

            // 3. 만료 시간 검증 (Optional - 이미 Google에서 해줌)
            Long exp = Long.parseLong((String) response.get("exp"));
            if (System.currentTimeMillis() / 1000 > exp) {
                return ResponseEntity.badRequest().body("Token has expired.");
            }

//            // 4. JWT가 유효하면 서버 측의 JWT 토큰 발급 (Optional)
            String accessJwtToken = jwtTokenProvider.createToken(email, name, picture);
            String refreshToken = jwtTokenProvider.createRefreshToken(email, name, picture);

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)  // 7일
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                    .body(Map.of( "email", email, "name", name, "picture", picture));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid token or error in validation.");
        }
    }


    @PostMapping("/google/callback")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam("code") String code) {
        GoogleUser user = googleOAuthService.getGoogleUserInfo(code);

        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.createToken(user.getEmail(), user.getName(), user.getProfileImageUrl());

        return ResponseEntity.ok(new GoogleAuthResponse(jwtToken, user));
    }
}
