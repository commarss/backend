package com.ll.commars.domain.restaurant.category.dto;

import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class RestaurantCategoryDto {
    // 카테고리 정보
    @Getter
    @Builder
    public static class RestaurantCategoryInfo {
        private Long id;
        private String name;
    }

    // 카테고리 등록 시 요청
    @Getter
    @Builder
    public static class RestaurantCategoryEnrollRequest {
        private String name;
    }

    // 식당에 카테고리 등록 및 수정 시 요청
    @Getter
    @Builder
    public static class RestaurantCategoryWriteRequest {
        private Long id;
    }

    // 카테고리를 기준으로 식당 조회 시 응답
    @Getter
    @Builder
    public static class ShowAllRestaurantsByCategoryResponse {
        private Long id;
        private String categoryName;
        private List<RestaurantDto.RestaurantInfo> restaurants;
    }

    // 존재하는 카테고리 조회 시 응답
    @Getter
    @Builder
    public static class ShowAllCategoriesResponse{
        private List<RestaurantCategoryInfo> categories;
    }

    // 특정 식당의 카테고리 조회 시 응답
    @Getter
    @Builder
    public static class ShowCategoryNameResponse {
        private String categoryName;
    }
}
