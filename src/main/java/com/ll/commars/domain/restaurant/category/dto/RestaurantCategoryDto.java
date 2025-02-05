package com.ll.commars.domain.restaurant.category.dto;

import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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

    @Getter
    @Builder
    public static class ShowAllRestaurantsResponse{
        private Long id;
        private String categoryName;
        private List<RestaurantDto.RestaurantInfo> restaurants;
    }

    @Getter
    @Builder
    public static class ShowAllCategoriesResponse{
        private List<RestaurantCategoryInfo> categories;
    }
}
