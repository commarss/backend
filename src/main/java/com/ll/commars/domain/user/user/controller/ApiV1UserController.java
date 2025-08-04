package com.ll.commars.domain.user.user.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.review.review.dto.ReviewDto;
import com.ll.commars.domain.user.favorite.dto.FavoriteDto;
import com.ll.commars.domain.user.user.dto.UserDto;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ApiV1UserController {

	private static final Logger logger = LoggerFactory.getLogger(ApiV1UserController.class);
	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody User request) {
		if (userService.isEmailTaken(request.getEmail())) {
			return ResponseEntity.badRequest().body("이미 사용 중인 이메일입니다.");
		}

		User newUser = new User();
		newUser.setEmail(request.getEmail());
		newUser.setName(request.getName());
		newUser.setGender(request.getGender());

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
}
