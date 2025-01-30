package com.ll.commars.domain.auth.google.service;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.ll.commars.domain.auth.google.entity.GoogleUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleOAuthService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String tokenUri = "https://oauth2.googleapis.com/token";
    private final String userInfoUri = "https://openidconnect.googleapis.com/v1/userinfo";

    public GoogleUser getGoogleUserInfo(String code) {
        // 1. 토큰 요청
        TokenResponse tokenResponse = getTokenFromCode(code);

        // 2. 사용자 정보 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenResponse.getAccessToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GoogleUser> response = restTemplate.exchange(
                userInfoUri, HttpMethod.GET, entity, GoogleUser.class
        );

        return response.getBody();
    }

    private TokenResponse getTokenFromCode(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", "your-google-client-id");
        params.add("client_secret", "your-google-client-secret");
        params.add("redirect_uri", "http://localhost:8080/auth/google/callback");
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params);
        return restTemplate.postForObject(tokenUri, request, TokenResponse.class);
    }
}
