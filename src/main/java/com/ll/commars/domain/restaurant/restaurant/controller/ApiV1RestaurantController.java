package com.ll.commars.domain.restaurant.restaurant.controller;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.service.RestaurantService;
import com.ll.commars.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurants")
public class ApiV1RestaurantController {
    private final RestaurantService restaurantService;
    @PostMapping("/write")
    public RsData<Restaurant> write(
            @RequestBody @Valid RestaurantsWriteRequest request
    ){
        Restaurant restaurant = restaurantService.write(request.name, request.details, request.averageRate);
        return new RsData<>("201", "식당 등록 성공", restaurant);
    }

    record RestaurantsWriteRequest(
            @NotBlank String name,
            @NotBlank String details,
            Double averageRate
    ) {}

    @GetMapping("/detail")
    public String getRestaurantDetail(@RequestParam String name) {
        return restaurantService.getRestaurantDetail(name);
    }
}
