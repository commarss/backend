package com.ll.commars.domain.restaurants.restaurants.controller;

import com.ll.commars.domain.restaurants.restaurants.entity.Restaurants;
import com.ll.commars.domain.restaurants.restaurants.service.RestaurantsService;
import com.ll.commars.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurants")
public class ApiV1RestaurantsController {
    private final RestaurantsService restaurantsService;
    @PostMapping("/write")
    public RsData<Restaurants> write(
            @RequestBody @Valid RestaurantsWriteRequest request
    ){
        Restaurants restaurants = restaurantsService.write(request.name, request.details, request.averageRate);
        return new RsData<>("201", "식당 등록 성공", restaurants);
    }

    record RestaurantsWriteRequest(
            @NotBlank String name,
            @NotBlank String details,
            Double averageRate
    ) {}

    @GetMapping("/detail")
    public String getRestaurantDetail(@RequestParam String name) {
        return restaurantsService.getRestaurantDetail(name);
    }
}
