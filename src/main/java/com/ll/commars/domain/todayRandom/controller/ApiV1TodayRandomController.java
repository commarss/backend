package com.ll.commars.domain.todayRandom.controller;


import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantSummaryDTO;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.review.review.dto.RestaurantReviewAnalysisDTO;
import com.ll.commars.domain.todayRandom.service.TodayRandomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping(value = "/api/v1/today-random", produces = APPLICATION_JSON_VALUE)
@Tag(name = "ApiV1BoardController", description = "오늘뭐먹지 관련 API")
public class ApiV1TodayRandomController {
    private final TodayRandomService todayRandomService;


    @Autowired
    public ApiV1TodayRandomController(TodayRandomService todayRandomService) {
        this.todayRandomService = todayRandomService;
    }

    @GetMapping("/random") //랜덤 5개 식당조회
    @Operation(summary = "회원 거리와 찜기반 랜덤 5개 식당조회")
    public List<RestaurantSummaryDTO> getRandomRestaurants(
            @RequestParam("lat") double lat,
            @RequestParam("lng") double lng,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.valueOf(userDetails.getUsername());
        return todayRandomService.getRandomRestaurants(lat, lng, userId);
    }

    // 프론트에서 선택한 1개의 식당 상세 정보 가져오기
    @GetMapping("/selected") //돌림판기능
    @Operation(summary = "돌림판: 1개식당정보")
    public Optional<RestaurantSummaryDTO> getSelectedRestaurant(@RequestParam("restaurantId") Long restaurantId) {
        return todayRandomService.getRestaurantDetails(restaurantId);
    }


    @GetMapping("/notuser/random") // 유저아닌 거리랜덤 5개 식당 조회
    @Operation(summary = "비회원 거리 랜덤 5개 식당 조회")
    public List<RestaurantSummaryDTO> getnotuserRandomRestaurants(
            @RequestParam("lat") double lat,
            @RequestParam("lng") double lng) {
        return todayRandomService.getnotuserRandomRestaurants(lat, lng);
    }


    // 모든 레스토랑에 대한 리뷰 분석 결과 반환
    @GetMapping("/review-analysis/{restaurantId}")
    @Operation(summary = "레스토랑 리뷰 분석 정보", description = "레스토랑에 대한 리뷰 개수와 평균 평점을 계산하여 반환합니다.")
    public RestaurantReviewAnalysisDTO getRestaurantReviewAnalysis(@PathVariable Long restaurantId) {
        return todayRandomService.getRestaurantReviewAnalysis(restaurantId);
    }

    @GetMapping("/review-analysis")
    @Operation(summary = "전체 레스토랑 리뷰 분석 순위", description = "전체 레스토랑을 리뷰 분석 점수 기준으로 순위를 매겨 반환합니다.")
    public List<RestaurantReviewAnalysisDTO> getAllRestaurantReviewAnalysis() {
        return todayRandomService.getAllRestaurantReviewAnalysis();
    }



}
