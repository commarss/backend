package com.ll.commars.domain.user.location.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/location")
public class ApiV1LocationController {

	@Value("${custom.ipInfo.token}")
	private String token;

	@GetMapping("/current")
	public ResponseEntity<?> getUserLocation(HttpServletRequest request) {
		String ipAdress = request.getHeader("X-FORWARDED-FOR");
		if (ipAdress == null || ipAdress.isEmpty()) {
			ipAdress = request.getRemoteAddr();
		}

		if (ipAdress.equals("0:0:0:0:0:0:0:1")) {
			ipAdress = "121.128.155.25";
		}

		// 테스트를 위해 token을 직접 하드코딩
		String url = "https://ipinfo.io/" + ipAdress + "?token=" + token;
		RestTemplate restTemplate = new RestTemplate();

		try {
			String locationInfo = restTemplate.getForObject(url, String.class);
			// Gson을 이용하여 JSON 문자열 파싱
			JsonObject jsonObject = JsonParser.parseString(locationInfo).getAsJsonObject();
			String loc = jsonObject.get("loc").getAsString(); // 예: "37.3860,-122.0838"

			// loc 값을 콤마 기준으로 분리하여 위도와 경도 추출
			String[] coordinates = loc.split(",");
			String latitude = coordinates[0].trim();
			String longitude = coordinates[1].trim();

			// 위도, 경도만 담을 응답 객체 생성 (Map 또는 별도의 POJO 사용 가능)
			Map<String, String> latLongResponse = new HashMap<>();
			latLongResponse.put("latitude", latitude);
			latLongResponse.put("longitude", longitude);

			// Gson을 사용하여 객체를 JSON 문자열로 변환
			Gson gson = new Gson();
			String jsonResponse = gson.toJson(latLongResponse);

			return ResponseEntity.ok().body(jsonResponse);
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
