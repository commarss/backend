package com.ll.commars.domain.user.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ll.commars.domain.favorite.favorite.dto.FavoriteDto;
import com.ll.commars.domain.review.review.dto.ReviewDto;
import com.ll.commars.domain.user.dto.UserDto;
import com.ll.commars.domain.user.entity.User;
import com.ll.commars.domain.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	// todo: 사용자의 게시글의 개수 세는 엔드포인트 구현

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private final UserService userService;

	@Value("${custom.ipInfo.token}")
	private String token;

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody User request) {
		if (userService.isEmailTaken(request.getEmail())) {
			return ResponseEntity.badRequest().body("이미 사용 중인 이메일입니다.");
		}

		User newUser = new User();

		userService.saveUser(newUser);
		return ResponseEntity.ok("회원가입이 완료되었습니다.");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user, HttpSession session) {
		logger.info("Login attempt for email: {}", user.getEmail());
		User authenticatedUser = userService.authenticate(user.getEmail());

		if (authenticatedUser != null) {
			logger.info("Login successful for email: {}", user.getEmail());

			// 로그인 정보를 DB에 저장하지 않고, 로그만 기록
			logger.info("User {} logged in at {}", authenticatedUser.getEmail(), java.time.LocalDateTime.now());

			// 세션에 사용자 정보 저장
			session.setAttribute("user", authenticatedUser);
			// 확인용 로그 추가
			logger.info("User stored in session: {}", authenticatedUser.getEmail());
			logger.info("Session user set: {}", session.getAttribute("user"));  // 추가 확인 로그
			System.out.println("세션정보: " + session.getAttribute("user"));

			return ResponseEntity.ok(authenticatedUser);
		}

		logger.warn("Login failed for email: {}", user.getEmail());
		return ResponseEntity.status(401).body("이메일 또는 비밀번호가 일치하지 않습니다.");
	}

	@GetMapping("/current-user")
	public ResponseEntity<?> getCurrentUser(HttpSession session) {
		User user = (User)session.getAttribute("user");
		if (user != null) {
			// 이메일을 JSON 형식으로 반환
			Map<String, String> response = new HashMap<>();
			response.put("email", user.getEmail());
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(401).body("로그인된 사용자가 없습니다.");
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpSession session) {
		session.invalidate();
		return ResponseEntity.ok("로그아웃되었습니다.");
	}

	// 내 위치 기반 식당 찾기

	@GetMapping("/favorites")
	public ResponseEntity<?> getFavoriteLists(
		@AuthenticationPrincipal UserDetails userDetails) {
		Optional<User> user = userService.findById(Long.parseLong(userDetails.getUsername()));
		if (user.isEmpty()) {
			return ResponseEntity
				.status(401)
				.body("로그인이 필요합니다.");
		}

		UserDto.UserFavoriteListsResponse response = userService.getFavoriteLists(user.get());
		return ResponseEntity
			.status(200)
			.body(response);
	}

	@PostMapping("/favorite")
	public ResponseEntity<String> addFavorite(
		@RequestBody FavoriteDto.CreateFavoriteListRequest request,
		HttpSession session) {
		User user = (User)session.getAttribute("user");
		if (user == null) {
			return ResponseEntity
				.status(401)
				.body("로그인이 필요합니다.");
		}

		userService.createFavoriteList(user, request);
		return ResponseEntity
			.status(201)
			.body("찜 리스트가 생성되었습니다.");
	}

	@GetMapping("/reviews")
	public ResponseEntity<?> getReviews(HttpSession session) {
		User user = (User)session.getAttribute("user");
		if (user == null) {
			return ResponseEntity
				.status(401)
				.body("로그인이 필요합니다.");
		}

		ReviewDto.ShowAllReviewsResponse response = userService.getReviews(user.getId());

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
