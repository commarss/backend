package com.ll.commars.domain.home.TodayRandom.controller;


import com.ll.commars.domain.home.TodayRandom.dto.MyFavoriteRequest;
import com.ll.commars.domain.home.TodayRandom.dto.MypageRequest;
import com.ll.commars.domain.home.TodayRandom.entity.Restaurant;
import com.ll.commars.domain.home.TodayRandom.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // 추가
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/random")  //오늘뭐 먹지 랜덤 5개추천
    public List<String> getRandomRestaurants(
            @RequestParam("lat") double lat,
            @RequestParam("lng") double lng,
            @RequestParam("email") String email) {
        return restaurantService.getRandomRestaurants(lat, lng, email);
    }


    @GetMapping("/detail") //http://localhost:8080/api/restaurants/detail?name=식당 J 식당이름으로 세부사항 출력
    public Restaurant getRestaurantDetails(@RequestParam String name) {
        return restaurantService.getRestaurantDetails(name);
    }

    @GetMapping("/keywordtop10")
    public List<String> getTop10Keywords() { // 인기 키워드 Top 10
        return restaurantService.getTop10Keywords()
                .stream()
                .map(keyword -> keyword.split(",")[0]) // 숫자 제거 후 키워드만 추출
                .toList();
    }

    @GetMapping("/search") //http://localhost:8080/api/restaurants/search?keyword=파스타 해당키워드가 포함된 식당정보
    public List<Restaurant> searchByKeyword(@RequestParam String keyword) {
        return restaurantService.searchByKeyword(keyword);
    }

    @PostMapping("/mypage")
    public ResponseEntity<String> saveReview(@RequestBody MypageRequest request) {
        // keywords를 리스트로 변환
        List<String> keywordList = Arrays.asList(request.getKeywords().split("\\s*,\\s*")); // 콤마+공백 제거

        restaurantService.saveMypage(
                request.getUser().getEmail(),
                request.getRestaurantId(),
                request.getReviewer(),
                keywordList,  // 변환된 리스트 전달
                request.getReview(),
                request.getScore()
        );
        return ResponseEntity.ok("리뷰가 저장되었습니다.");
    }


    @PostMapping("/mypage/favorites") //찜목록추가
    public ResponseEntity<Map<String, String>> addToFavorites(@RequestBody MyFavoriteRequest request) {
        boolean isAdded = restaurantService.saveToFavorites(request.getEmail(), request.getRestaurantId());

        Map<String, String> response = new HashMap<>();
        if (isAdded) {
            response.put("message", "찜 목록에 추가되었습니다!");
        } else {
            response.put("message", "이미 찜 목록에 존재하는 식당입니다.");
        }
        return ResponseEntity.ok(response);
    }


    // 찜 목록 조회
    @GetMapping("/mypage/favorites")
    public List<Restaurant> getFavoriteRestaurants(@RequestParam String email) {
        return restaurantService.getFavoriteRestaurants(email);
    }

    //내위치기반 식당이름찾기
    @GetMapping("/nearby")
    public List<String> getNearbyRestaurants(@RequestParam double lat, @RequestParam double lng) {
        List<Restaurant> restaurants = restaurantService.getNearbyRestaurants(lat, lng, 2000);

        return restaurants.stream()
                .map(Restaurant::getName)
                .collect(Collectors.toList());
    }




    // 찜 목록 개수 조회
    @GetMapping("/mypage/favorites/count")
    public ResponseEntity<Map<String, Integer>> getFavoriteCount(@RequestParam String email) {
        int count = restaurantService.getFavoriteCount(email);
        Map<String, Integer> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
}
//Postman에서 요청하면 반경 2km, 찜 목록, 겹치는 키워드 조건에 해당하는 식당 이름 5개가 랜덤으로 반환됩니다.