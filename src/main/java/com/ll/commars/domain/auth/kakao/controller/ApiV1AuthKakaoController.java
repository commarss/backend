package com.ll.commars.domain.auth.kakao.controller;

import com.ll.commars.domain.auth.auth.service.AuthService;
import com.ll.commars.domain.auth.kakao.service.KakaoService;
import com.ll.commars.domain.user.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/auth", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1AuthKakaoController", description = "카카오 로그인 API")
public class ApiV1AuthKakaoController {
    private final KakaoService kakaoService;
    private final AuthService authService;

    /**
     * 카카오 로그인을 위한 엔드포인트
     * 요청 바디 예시: { "code": "인가코드" }
     */
    @PostMapping(value = "/login/kakao", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "카카오 로그인")
    public ResponseEntity<?> loginForKakao(@RequestBody Map<String, String> body, HttpSession session) {
        System.out.println("Request body = " + body);
        String code = body.get("code");
        System.out.println("Received code = " + code);

        // 세션에서 이미 사용한 인가코드인지 확인하여 중복 사용 방지
        if (session.getAttribute("usedKakaoCode") != null && session.getAttribute("usedKakaoCode").equals(code)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("이미 사용된 인가코드입니다.");
        }
        // 현재 인가코드를 세션에 저장
        session.setAttribute("usedKakaoCode", code);

        // 인가 코드를 통해 access token 발급
        String kakaoAccessToken = kakaoService.getAccessToken(code);
        System.out.println("Kakao accessToken = " + kakaoAccessToken);

        if (kakaoAccessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("토큰 발급 실패");
        }

        // access token을 이용해 사용자 프로필 조회
        Map<String, Object> userProfile = kakaoService.getUserProfile(kakaoAccessToken);
        System.out.println("Kakao userProfile = " + userProfile);

        // 사용자 프로필 정보를 기반으로 User 엔티티 생성 및 로그인
        User kakaoUser = kakaoService.loginForKakao(userProfile);
        return authService.login(kakaoUser);
    }
}
