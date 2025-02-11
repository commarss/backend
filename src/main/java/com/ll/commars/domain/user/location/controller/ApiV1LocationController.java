package com.ll.commars.domain.user.location.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

        return ResponseEntity.ok(locationInfo);
    }
}
