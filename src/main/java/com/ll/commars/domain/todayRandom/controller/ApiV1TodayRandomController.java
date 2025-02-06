package com.ll.commars.domain.todayRandom.controller;


import com.ll.commars.domain.todayRandom.service.TodayRandomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/today-random", produces = APPLICATION_JSON_VALUE)
public class ApiV1TodayRandomController {
    private TodayRandomService todayRandomService;

//    @GetMapping("/random")  //오늘뭐 먹지 랜덤 5개추천
//    public List<String> getRandomRestaurants(
//            @RequestParam("lat") double lat,
//            @RequestParam("lng") double lng,
//            @RequestParam("email") String email) {
//        return todayRandomService.getRandomRestaurants(lat, lng, email);
//    }
}