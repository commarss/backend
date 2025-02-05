package com.ll.commars.domain.auth.naver.service;

import com.ll.commars.domain.member.member.entity.Member;
import com.ll.commars.domain.member.member.service.MemberService;
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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NaverService {
    private final MemberService memberService;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String redirectUri;

    private final String state = UUID.randomUUID().toString();

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

    public Member loginForNaver(Map<String, Object> userProfile) {
        String email = (String) userProfile.get("email");
        String name = (String) userProfile.get("name");
        String profile = (String) userProfile.get("profile_image");

        if (email == null || name == null || profile == null) {
            return null;
        }

        Member naverMember = new Member().builder()
                .email(email)
                .name(name)
                .profile(profile)
                .build();

        return memberService.accessionCheck(naverMember);
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
