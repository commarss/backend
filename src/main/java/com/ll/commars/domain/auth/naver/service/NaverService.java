package com.ll.commars.domain.auth.naver.service;

import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NaverService {
    private final UserService userService;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    public String getAccessToken(String code, String state) {
        String tokenUrl = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code"
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&code=" + code
                + "&state=" + state;

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.getForEntity(tokenUrl, Map.class);

            if (response.getStatusCodeValue() != 200) {
                System.err.println("response = " + response);
                return null;
            }

            return (String) response.getBody().get("access_token");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> getUserProfile(String accessToken) {
        String profileUrl = "https://openapi.naver.com/v1/nid/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(profileUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, Object>>() {});


        return (Map<String, Object>) response.getBody().get("response");
    }

    public User loginForNaver(Map<String, Object> userProfile) {
        User naverUser = User.builder()
                .socialProvider(2)
                .phoneNumber((String) userProfile.get("mobile"))
                .email((String) userProfile.get("email"))
                .name((String) userProfile.get("name"))
                .profileImageUrl((String) userProfile.get("profile_image"))
                .build();

        return userService.accessionCheck(naverUser);
    }

    public String logout(String accessToken) {
        String logoutUrl = "https://nid.naver.com/oauth2.0/token?grant_type=delete"
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&access_token=" + accessToken
                + "&service_provider=NAVER";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(logoutUrl, HttpMethod.GET, null, Map.class);

        return (String) response.getBody().get("result");
    }
}
