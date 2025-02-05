package com.ll.commars.domain.restaurant.category.controller;

import com.ll.commars.domain.restaurant.category.dto.RestaurantCategoryDto;
import com.ll.commars.domain.restaurant.category.service.RestaurantCategoryService;
import com.ll.commars.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/restaurant-category", produces = APPLICATION_JSON_VALUE)
public class ApiV1RestaurantCategoryController {
    private final RestaurantCategoryService restaurantCategoryService;

    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "식당 카테고리 등록")
    public RsData<RestaurantCategoryDto.RestaurantCategoryInfo> writeCategory(
            @RequestBody @Valid RestaurantCategoryDto.RestaurantCategoryWriteRequest request
    ) {
        RestaurantCategoryDto.RestaurantCategoryInfo response = restaurantCategoryService.writeCategory(request);
        return new RsData<>("201", "식당 카테고리 등록 성공", response);
    }
}

