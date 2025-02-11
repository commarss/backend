// KakaoService.java
package com.ll.commars.domain.auth.kakao.service;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final UserService userService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    public String getAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, Map.class);

            if (response.getStatusCodeValue() != 200) {
                System.err.println("토큰 요청 실패: " + response);
                return null;
            }

            return (String) response.getBody().get("access_token");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> getUserProfile(String accessToken) {
        String profileUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                profileUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        return response.getBody();
    }

    /**
     * Kakao의 사용자 프로필 정보를 기반으로 내부 User 엔티티로 변환하고, 가입/로그인 처리
     */
    public User loginForKakao(Map<String, Object> userProfile) {
        // Kakao API에서 제공하는 고유 식별자인 id값 추출
        Long kakaoId = ((Number) userProfile.get("id")).longValue();

        // kakao_account와 profile 데이터 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) userProfile.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String nickname = (String) profile.get("nickname");
        String profileImageUrl = (String) profile.get("profile_image_url");
        String email = kakaoId + "@kakao.com"; // 카카오에서 이메일 제공x, 카카오 고유 id로 임시 이메일 생성하기

        // User 엔티티 생성
        User kakaoUser = User.builder()
                .socialProvider(1)
                .kakaoId(kakaoId)
                .email("kakao@email"+nickname)          // 이메일이 필요하면 사용 (현재 주석 처리)
                .name(nickname)
                .profileImageUrl(profileImageUrl)
                .build();

        return userService.accessionKakaoCheck(kakaoUser);
    }
}