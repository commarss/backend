package com.ll.commars.domain.auth.google.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ApiV1AuthGoogleController {

    @GetMapping("login-success")
    public Map<String, Object> loginSuccess(@AuthenticationPrincipal OAuth2User oAuth2User) {
        // 사용자 정보 반환
        return oAuth2User.getAttributes();
    }
}
