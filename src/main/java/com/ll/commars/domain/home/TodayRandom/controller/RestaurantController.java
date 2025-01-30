package com.ll.commars.domain.home.TodayRandom.controller;


import com.ll.commars.domain.home.TodayRandom.entity.Restaurant;
import com.ll.commars.domain.home.TodayRandom.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
}
//Postman에서 요청하면 반경 2km, 찜 목록, 겹치는 키워드 조건에 해당하는 식당 이름 5개가 랜덤으로 반환됩니다.