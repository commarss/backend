package com.ll.commars.domain.todayRandom.controller;


import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantSummaryDTO;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.todayRandom.service.TodayRandomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Operation(summary = "랜덤 5개 식당조회")
    public List<RestaurantSummaryDTO> getRandomRestaurants(
            @RequestParam("lat") double lat,
            @RequestParam("lng") double lng,
            @RequestParam("id") Long id) {
        return todayRandomService.getRandomRestaurants(lat, lng, id);
    }

    // 프론트에서 선택한 1개의 식당 상세 정보 가져오기
    @GetMapping("/selected") //돌림판기능
    @Operation(summary = "돌림판: 1개식당정보")
    public Optional<RestaurantSummaryDTO> getSelectedRestaurant(@RequestParam("restaurantId") Long restaurantId) {
        return todayRandomService.getRestaurantDetails(restaurantId);
    }


    // 랜덤 5개 중 1개 선택하여 상세 정보 반환 (RestaurantSummaryDTO 사용)
    /*
    @GetMapping("/selected")
    public Optional<RestaurantSummaryDTO> getRandomSelectedRestaurant(
            @RequestParam("lat") double lat,
            @RequestParam("lng") double lng,
            @RequestParam("id") Long userId) {
        return todayRandomService.getRandomRestaurantDetails(lat, lng, userId);
    }*/



}
