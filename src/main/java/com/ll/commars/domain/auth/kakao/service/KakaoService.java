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
        // Kakao API의 응답 예시에서 root에 "id"가 있습니다.
        Long kakaoId = ((Number) userProfile.get("id")).longValue();

        Map<String, Object> kakaoAccount = (Map<String, Object>) userProfile.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        System.out.println(profile);

        // 이메일은 제공되지 않을 수도 있으므로, 고유한 id를 사용합니다.
        // String email = (String) kakaoAccount.get("email"); // 필요한 경우 사용

        String nickname = (String) profile.get("nickname");
        String profileImageUrl = (String) profile.get("profile_image_url");

        User kakaoUser = User.builder()
                .socialProvider(1)
//                .id(Long.valueOf(kakaoId))
//                .socialId(String.valueOf(kakaoId))  // Kakao 고유 id를 문자열로 저장
                .email("kakao@email"+nickname)          // 이메일이 필요하면 사용 (현재 주석 처리)
                .name(nickname)
                .profileImageUrl(profileImageUrl)
                .build();

        return userService.accessionKakaoCheck(kakaoUser);
    }
}