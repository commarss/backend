package com.ll.commars.domain.todayRandom.controller;


import com.ll.commars.domain.todayRandom.service.TodayRandomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // 추가
@RestController
@RequestMapping("/api/v1/today-random")
public class ApiV1TodayRandomController {
    @Autowired
    private TodayRandomService todayRandomService;

//    @GetMapping("/random")  //오늘뭐 먹지 랜덤 5개추천
//    public List<String> getRandomRestaurants(
//            @RequestParam("lat") double lat,
//            @RequestParam("lng") double lng,
//            @RequestParam("email") String email) {
//        return todayRandomService.getRandomRestaurants(lat, lng, email);
//    }
}