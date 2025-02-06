package com.ll.commars.domain.auth.google.controller;

import com.ll.commars.domain.auth.auth.service.AuthService;
import com.ll.commars.domain.auth.google.service.GoogleService;
import com.ll.commars.domain.user.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth", produces = APPLICATION_JSON_VALUE)
@Tag(name = "ApiV1AuthGoogleController", description = "구글 로그인 API")
public class ApiV1AuthGoogleController {
    private final GoogleService googleService;
    private final AuthService  authService;

    @PostMapping(value = "/login/google", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "구글 ID Token 검증")
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
