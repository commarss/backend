package com.ll.commars.domain.restaurant.restaurant.controller;

import com.ll.commars.domain.restaurant.menu.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.menu.service.RestaurantMenuService;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantDto;
import com.ll.commars.domain.restaurant.restaurant.service.RestaurantService;
import com.ll.commars.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurant")
public class ApiV1RestaurantController {
    private final RestaurantService restaurantService;
    private final RestaurantMenuService restaurantMenuService;

    // 식당 정보 등록
    @PostMapping("/")
    public RsData<RestaurantDto.RestaurantWriteResponse> write(
            @RequestBody @Valid RestaurantDto.RestaurantWriteRequest request
    ){
        RestaurantDto.RestaurantWriteResponse response = restaurantService.write(request);
        return new RsData<>("201", "식당 등록 성공", response);
    }

    // 식당 메뉴 등록
    @PostMapping("/menu")
    public RsData<RestaurantMenuDto.RestaurantMenuWriteResponse> writeMenu(
            @RequestBody @Valid RestaurantMenuDto.RestaurantMenuWriteRequest request
    ){
        RestaurantMenuDto.RestaurantMenuWriteResponse response = restaurantMenuService.write(request);

        return new RsData<>("201", "메뉴 등록 성공", response);
    }
    // 모든 식당 조회
    @GetMapping("/")
    public RsData<RestaurantDto.RestaurantShowAllResponse> getRestaurants() {
        RestaurantDto.RestaurantShowAllResponse response = restaurantService.getRestaurants();
        return new RsData<>("200", "모든 식당 조회 성공", response);
    }
}
