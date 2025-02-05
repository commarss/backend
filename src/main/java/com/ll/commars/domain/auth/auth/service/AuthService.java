package com.ll.commars.domain.auth.auth.service;

import com.ll.commars.domain.auth.auth.entity.AuthResponse;
import com.ll.commars.domain.auth.auth.entity.AuthUser;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.global.jwt.component.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;

    public ResponseEntity<?> login(User user) {
        AuthUser authUser = toAuthUser(user);

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

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
                .body(AuthResponse.builder()
                        .accessToken(accessToken)
                        .authUser(authUser)
                        .build());
    }

    public AuthUser toAuthUser(User user) {
        return AuthUser.builder()
                .name(user.getName())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .build();
    }

}
