package com.ll.commars.domain.restaurant.restaurant.controller;

import com.ll.commars.domain.restaurant.menu.dto.RestaurantMenuDTO;
import com.ll.commars.domain.restaurant.menu.entity.RestaurantMenu;
import com.ll.commars.domain.restaurant.menu.service.RestaurantMenuService;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.service.RestaurantService;
import com.ll.commars.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurants")
public class ApiV1RestaurantController {
    private final RestaurantService restaurantService;
    private final RestaurantMenuService restaurantMenuService;

    // 식당 정보 등록
    @PostMapping("/")
    public RsData<Restaurant> write(
            @RequestBody @Valid RestaurantsWriteRequest request
    ){
        Restaurant restaurant = restaurantService.write(
                request.name,
                request.details,
                request.averageRate,
                request.imageUrl,
                request.contact,
                request.address,
                request.lat,
                request.lng,
                request.runningState,
                request.summarizedReview
        );
        return new RsData<>("201", "식당 등록 성공", restaurant);
    }

    record RestaurantsWriteRequest(
            @NotBlank String name,
            @NotBlank String details,
            Double averageRate,
            String imageUrl,
            String contact,
            @NotBlank String address,
            @NotNull Double lat,
            @NotNull Double lng,
            Boolean runningState,
            String summarizedReview
    ) {}

//    @GetMapping("/detail")
//    public String getRestaurantDetail(@RequestParam String name) {
//        return restaurantService.getRestaurantDetail(name);
//    }

    // 식당 메뉴 등록
    @PostMapping("/menu")
    public RsData<RestaurantMenuDTO> writeMenu(
            @RequestBody @Valid RestaurantMenuWriteRequest request
    ){
        RestaurantMenuDTO menuDto = restaurantMenuService.write(
                request.name,
                request.price,
                request.imageUrl,
                request.restaurantName
        );

        return new RsData<>("201", "메뉴 등록 성공", menuDto);
    }

    record RestaurantMenuWriteRequest(
            @NotBlank String name,
            @NotNull Integer price,
            String imageUrl,
            String restaurantName
    ) {}
}
