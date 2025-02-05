package com.ll.commars.domain.restaurant.category.dto;

import lombok.Builder;
import lombok.Getter;

public class RestaurantCategoryDto {
    @Getter
    @Builder
    public static class RestaurantCategoryInfo {
        private Long id;
        private String name;
    }

    @Getter
    @Builder
    public static class RestaurantCategoryEnrollRequest {
        private String name;
    }

    @Getter
    @Builder
    public static class RestaurantCategoryWriteRequest {
        private Long id;
    }
}
