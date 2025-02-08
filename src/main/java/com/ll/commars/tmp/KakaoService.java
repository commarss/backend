package com.ll.commars.tmp;
/*
import com.ll.commars.domain.home.loginfunction.dto.KakaoTokenResponseDto;
import com.ll.commars.domain.home.loginfunction.dto.KakaoUserInfoResponseDto;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {

    private String clientId;
    private final String redirectUri = "http://localhost:8080/callback"; // 실제 redirect URI를 여기에 설정
    private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";

    @Value("${kakao.client_id}")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAccessTokenFromKakao(String code) {
        KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("redirect_uri", redirectUri)  // redirect_uri 추가
                        .queryParam("code", code)
                        //.queryParam("client_secret", "7bmR3z78pm8z7kJbxLPhi9gEAHrEjV45")
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.error("4xx Client Error: {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Invalid Parameter"));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.error("5xx Server Error: {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Internal Server Error"));
                })
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();

        if (kakaoTokenResponseDto == null || kakaoTokenResponseDto.getAccessToken() == null) {
            throw new RuntimeException("Access token을 받을 수 없습니다.");
        }

        log.info("Access Token: {}", kakaoTokenResponseDto.getAccessToken());
        log.info("Refresh Token: {}", kakaoTokenResponseDto.getRefreshToken());
        log.info("Id Token: {}", kakaoTokenResponseDto.getIdToken());
        log.info("Scope: {}", kakaoTokenResponseDto.getScope());

        return kakaoTokenResponseDto.getAccessToken();
    }

    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        KakaoUserInfoResponseDto userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.error("4xx Client Error: {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Invalid Parameter"));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.error("5xx Server Error: {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Internal Server Error"));
                })
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();

        if (userInfo == null) {
            throw new RuntimeException("사용자 정보를 받을 수 없습니다.");
        }

        log.info("User ID: {}", userInfo.getId());
        log.info("User Nickname: {}", userInfo.getKakaoAccount().getProfile().getNickName());
        log.info("User Profile Image: {}", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());

        return userInfo;
    }

    public String getLocationLogout() {
        return "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="
                + clientId + "&redirect_uri=" + redirectUri + "&prompt=login";
    }
}


 */