package com.ll.commars.domain.auth.auth.controller;

import com.ll.commars.global.jwt.component.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1AuthController", description = "인증 관련 API")
public class ApiV1AuthController {
    private final JwtProvider jwtProvider;

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(Map.of("server", "logout ok"));
    }
}
