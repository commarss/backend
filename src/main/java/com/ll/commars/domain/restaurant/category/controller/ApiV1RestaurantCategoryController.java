package com.ll.commars.domain.restaurant.category.controller;

import com.ll.commars.domain.restaurant.category.dto.RestaurantCategoryDto;
import com.ll.commars.domain.restaurant.category.service.RestaurantCategoryService;
import com.ll.commars.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/restaurant-category", produces = APPLICATION_JSON_VALUE)
public class ApiV1RestaurantCategoryController {
    private final RestaurantCategoryService restaurantCategoryService;

    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "식당 카테고리 등록")
    public RsData<RestaurantCategoryDto.RestaurantCategoryInfo> writeCategory(
            @RequestBody @Valid RestaurantCategoryDto.RestaurantCategoryEnrollRequest request
    ) {
        RestaurantCategoryDto.RestaurantCategoryInfo response = restaurantCategoryService.writeCategory(request);
        return new RsData<>("201", "식당 카테고리 등록 성공", response);
    }

    @GetMapping("/")
    @Operation(summary = "모든 카테고리 조회")
    public RsData<RestaurantCategoryDto.ShowAllCategoriesResponse> getCategories() {
        RestaurantCategoryDto.ShowAllCategoriesResponse response = restaurantCategoryService.getCategories();
        return new RsData<>("200", "모든 카테고리 조회 성공", response);
    }

    @GetMapping("/{category_id}")
    @Operation(summary = "특정 카테고리에 속한 식당 조회")
    public RsData<RestaurantCategoryDto.ShowAllRestaurantsByCategoryResponse> getRestaurantByCategory(
            @PathVariable("category_id") Long categoryId
    ) {
        RestaurantCategoryDto.ShowAllRestaurantsByCategoryResponse response = restaurantCategoryService.getRestaurantByCategory(categoryId);
        return new RsData<>("200", "특정 카테고리에 속한 식당 조회 성공", response);
    }
}

