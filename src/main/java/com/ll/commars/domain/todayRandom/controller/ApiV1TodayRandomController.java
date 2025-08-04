package com.ll.commars.domain.todayRandom.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantSummaryDTO;
import com.ll.commars.domain.review.review.dto.RestaurantReviewAnalysisDTO;
import com.ll.commars.domain.todayRandom.service.TodayRandomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/today-random")
public class ApiV1TodayRandomController {

	private final TodayRandomService todayRandomService;

	@GetMapping("/random") //랜덤 5개 식당조회
	public List<RestaurantSummaryDTO> getRandomRestaurants(
		@RequestParam("lat") double lat,
		@RequestParam("lon") double lon,
		@AuthenticationPrincipal UserDetails userDetails) {
		Long userId = Long.valueOf(userDetails.getUsername());
		return todayRandomService.getRandomRestaurants(lat, lon, userId);
	}

	// 프론트에서 선택한 1개의 식당 상세 정보 가져오기
	@GetMapping("/selected") //돌림판기능
	public Optional<RestaurantSummaryDTO> getSelectedRestaurant(@RequestParam("restaurantId") Long restaurantId) {
		return todayRandomService.getRestaurantDetails(restaurantId);
	}

	@GetMapping("/notuser/random") // 유저아닌 거리랜덤 5개 식당 조회
	public List<RestaurantSummaryDTO> getnotuserRandomRestaurants(
		@RequestParam("lat") double lat,
		@RequestParam("lon") double lon) {
		return todayRandomService.getnotuserRandomRestaurants(lat, lon);
	}

	// 모든 레스토랑에 대한 리뷰 분석 결과 반환
	@GetMapping("/review-analysis/{restaurantId}")
	public RestaurantReviewAnalysisDTO getRestaurantReviewAnalysis(@PathVariable Long restaurantId) {
		return todayRandomService.getRestaurantReviewAnalysis(restaurantId);
	}

	@GetMapping("/review-analysis")
	public List<RestaurantReviewAnalysisDTO> getAllRestaurantReviewAnalysis() {
		return todayRandomService.getAllRestaurantReviewAnalysis();
	}

}
