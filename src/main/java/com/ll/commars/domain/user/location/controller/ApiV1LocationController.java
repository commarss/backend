package com.ll.commars.domain.user.location.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ApiV1LocationController {
    @Value("${spring.ipInfo.token}")
    private String token;

    @GetMapping("/api/v1/user/location/current")
    public ResponseEntity<?> getUserLocation(HttpServletRequest request) {
        String ipAdress = request.getHeader("X-FORWARDED-FOR");
        if (ipAdress == null || ipAdress.isEmpty()) {
            ipAdress = request.getRemoteAddr();
        }

        // 테스트를 위해 token을 직접 하드코딩
        String url = "https://ipinfo.io/" + ipAdress + "?token=14a73545c4250f";
        RestTemplate restTemplate = new RestTemplate();

        try {
            String locationInfo = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok().body(locationInfo);
        } catch (HttpClientErrorException e) {
            // ipinfo API 호출 시 발생한 HTTP 에러(예: 404 등)에 대한 처리
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("status", e.getStatusCode().value());
            errorDetails.put("error", e.getStatusText());
            errorDetails.put("message", e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(errorDetails);
        } catch (Exception e) {
            // 그 외의 예외에 대한 처리
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("status", 500);
            errorDetails.put("error", "Internal Server Error");
            errorDetails.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorDetails);
        }
    }
}
