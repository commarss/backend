package com.ll.commars.domain.member.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ll.commars.domain.favorite.favorite.dto.FavoriteDto;
import com.ll.commars.domain.member.dto.MemberDto;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.service.MemberService;
import com.ll.commars.domain.review.dto.ReviewDto;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

	// todo: 사용자의 게시글의 개수 세는 엔드포인트 구현

	private final MemberService memberService;

	@Value("${custom.ipInfo.token}")
	private String token;

	@GetMapping("/current-user")
	public ResponseEntity<?> getCurrentUser(HttpSession session) {
		Member member = (Member)session.getAttribute("user");
		if (member != null) {
			// 이메일을 JSON 형식으로 반환
			Map<String, String> response = new HashMap<>();
			response.put("email", member.getEmail());
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(401).body("로그인된 사용자가 없습니다.");
	}

	// 내 위치 기반 식당 찾기

	@GetMapping("/favorites")
	public ResponseEntity<?> getFavoriteLists(
		@AuthenticationPrincipal UserDetails userDetails) {
		Optional<Member> user = memberService.findById(Long.parseLong(userDetails.getUsername()));
		if (user.isEmpty()) {
			return ResponseEntity
				.status(401)
				.body("로그인이 필요합니다.");
		}

		MemberDto.UserFavoriteListsResponse response = memberService.getFavoriteLists(user.get());
		return ResponseEntity
			.status(200)
			.body(response);
	}

	@PostMapping("/favorite")
	public ResponseEntity<String> addFavorite(
		@RequestBody FavoriteDto.CreateFavoriteListRequest request,
		HttpSession session) {
		Member member = (Member)session.getAttribute("user");
		if (member == null) {
			return ResponseEntity
				.status(401)
				.body("로그인이 필요합니다.");
		}

		memberService.createFavoriteList(member, request);
		return ResponseEntity
			.status(201)
			.body("찜 리스트가 생성되었습니다.");
	}

	@GetMapping("/reviews")
	public ResponseEntity<?> getReviews(HttpSession session) {
		Member member = (Member)session.getAttribute("user");
		if (member == null) {
			return ResponseEntity
				.status(401)
				.body("로그인이 필요합니다.");
		}

		ReviewDto.ShowAllReviewsResponse response = memberService.getReviews(member.getId());

		return ResponseEntity
			.status(200)
			.body(response);
	}

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
