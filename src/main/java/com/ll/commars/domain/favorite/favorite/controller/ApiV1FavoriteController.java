package com.ll.commars.domain.favorite.favorite.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.favorite.favorite.entity.Favorite;
import com.ll.commars.domain.favorite.favorite.service.FavoriteRestaurantService;
import com.ll.commars.domain.favorite.favorite.service.FavoriteService;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorite")
public class ApiV1FavoriteController {

	private final FavoriteService favoriteService;
	private final MemberService memberService;
	private final FavoriteRestaurantService favoriteRestaurantService;

	// 찜 목록 개수 조회
	@GetMapping("/count")
	public ResponseEntity<Map<String, Integer>> getFavoriteCount(@RequestParam String email) {
		int count = favoriteService.getFavoriteCount(email);
		Map<String, Integer> response = new HashMap<>();
		response.put("count", count);
		return ResponseEntity.ok(response);
	}

	// @GetMapping("/{favorite_id}")
	// public ResponseEntity<FavoriteDto.FavoriteInfo> getFavorite(@PathVariable("favorite_id") Long favoriteId) {
	// 	FavoriteDto.FavoriteInfo response = favoriteService.getFavorite(favoriteId);
	// 	return ResponseEntity.ok(response);
	// }

	@DeleteMapping("/{favorite_id}")
	public ResponseEntity<String> deleteFavorite(@PathVariable("favorite_id") Long favoriteId) {
		favoriteService.deleteFavorite(favoriteId);
		return ResponseEntity.ok("찜 목록 삭제 성공");
	}

	// @PostMapping("/{favorite_id}/restaurant")
	// public ResponseEntity<FavoriteDto.FavoriteInfo> addRestaurantToFavorite(
	// 	@PathVariable("favorite_id") Long favoriteId,
	// 	@RequestBody FavoriteDto.AddRestaurantRequest request
	// ) {
	// 	FavoriteDto.FavoriteInfo response = favoriteService.addRestaurantToFavorite(favoriteId,
	// 		request.getRestaurantId());
	// 	return ResponseEntity.ok(response);
	// }

	// @PostMapping("/restaurant/add")
	// public ResponseEntity<?> addRestaurantToFavoriteList(
	// 	@RequestBody Map<String, Object> request
	// ) {
	// 	String favoriteId = String.valueOf(request.get("favoriteId"));
	// 	String restaurantId = String.valueOf(request.get("restaurantId"));
	// 	FavoriteDto.FavoriteInfo response = favoriteService.addRestaurantToFavorite(Long.parseLong(favoriteId),
	// 		Long.parseLong(restaurantId));
	// 	return ResponseEntity.ok(response);
	// }

	@PostMapping("/create")
	public ResponseEntity<?> createFavoriteList(
		@RequestBody Map<String, Object> request,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		System.out.println("request = " + request);
		String userId = userDetails.getUsername();
		return memberService.createFavoriteList(String.valueOf(request.get("name")), userId);
	}

	@GetMapping("/check")
	public ResponseEntity<?> checkFavorite(
		@RequestParam("restaurantId") Long restaurantId,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		Member member = memberService.findById(Long.parseLong(userDetails.getUsername()))
			.orElseThrow(() -> new IllegalArgumentException("User not found"));
		Optional<Favorite> isFavorite = favoriteService.isFavorite(member, restaurantId);
		Map<String, Long> response = new HashMap<>();

		if (isFavorite.isEmpty()) {
			response.put("isFavorite", null);
			return ResponseEntity.ok(response);
		}
		response.put("isFavorite", isFavorite.get().getId());
		return ResponseEntity.ok(response);
	}

	// @GetMapping
	// public ResponseEntity<List<FavoriteDto.FavoriteInfo>> getFavorites(
	// 	@AuthenticationPrincipal UserDetails userDetails) {
	// 	Long userId = Long.valueOf(userDetails.getUsername()); // 현재 로그인한 사용자 정보 가져오기
	// 	List<FavoriteDto.FavoriteInfo> favoriteList = favoriteService.getAllFavoritesByUser(userId);
	// 	return ResponseEntity.ok(favoriteList);
	// }

	@PostMapping("/delete/restaurant")
	public ResponseEntity<?> deleteRestaurantFromFavorite(
		@RequestBody Map<String, Object> request
	) {
		String favoriteId = String.valueOf(request.get("favoriteId"));
		String restaurantId = String.valueOf(request.get("restaurantId"));
		favoriteRestaurantService.deleteRestaurantFromFavorite(Long.parseLong(favoriteId),
			Long.parseLong(restaurantId));

		return new ResponseEntity<>(Map.of("message", "식당 삭제 성공"), org.springframework.http.HttpStatus.OK);
	}

}
