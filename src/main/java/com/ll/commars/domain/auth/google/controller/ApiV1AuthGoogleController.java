package com.ll.commars.domain.auth.google.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ApiV1AuthGoogleController {

    private static final String CLIENT_ID = "360427694540-i4i2593i1u708vfhc15uts2hpri1r467.apps.googleusercontent.com";

    @PostMapping("/google")
    public ResponseEntity<Map<String, Object>> verifyGoogleToken(@RequestBody Map<String, String> request) {
        try {
            String idTokenString = request.get("token");

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            ).setAudience(Collections.singletonList(CLIENT_ID)).build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");

                Map<String, Object> response = Map.of(
                        "email", email,
                        "name", name,
                        "picture", pictureUrl
                );

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid Google ID Token"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Token verification failed"));
        }
    }


    @GetMapping("login-success")
    public Map<String, Object> loginSuccess(@AuthenticationPrincipal OAuth2User oAuth2User) {
        // 사용자 정보 반환
        return oAuth2User.getAttributes();
    }
}
