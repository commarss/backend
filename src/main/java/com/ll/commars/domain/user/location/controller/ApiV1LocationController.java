package com.ll.commars.domain.user.location.controller;

import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/user", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ApiV1LocationController {
    @Value("${spring.ipInfo.token}")
    private String token;

    @GetMapping("/location")
    public ResponseEntity<?> getUserLocation(HttpServletRequest request) {
        String ipAdress = request.getHeader("X-FORWARDED-FOR");
        if (ipAdress == null || ipAdress.isEmpty()) {
            ipAdress = request.getRemoteAddr();
        }

        String url = "https://ipinfo.io/" + ipAdress + "?token=" + token;

        RestTemplate restTemplate = new RestTemplate();
        String locationInfo = restTemplate.getForObject(url, String.class);

        JsonObject jsonObject = JsonParser.parseString(Objects.requireNonNull(locationInfo)).getAsJsonObject();
        String loc = jsonObject.get("loc").getAsString(); // 예: "37.3860,-122.0838"

        // loc 값을 콤마 기준으로 분리하여 위도와 경도 추출
        String[] coordinates = loc.split(",");
        String latitude = coordinates[0].trim();
        String longitude = coordinates[1].trim();

        // 위도, 경도만 담을 응답 객체 생성 (Map 또는 별도의 POJO 사용 가능)
        Map<String, String> location = new HashMap<>();
        location.put("latitude", latitude);
        location.put("longitude", longitude);

        return ResponseEntity.ok()
                .body(location);
    }
}
