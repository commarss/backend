package com.ll.commars.domain.restaurant.restaurant.dto;

import com.ll.commars.domain.restaurant.menu.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.menu.entity.RestaurantMenu;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.review.review.dto.ReviewDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class RestaurantDto {
    // 식당 정보
    @Getter
    @Builder
    public static class RestaurantInfo {
        private Long id;
        private String name;
        private String details;
        private Double averageRate;
        private String imageUrl;
        private String contact;
        private String address;
        private Double lat;
        private Double lng;
        private Boolean runningState;
        private String summarizedReview;
        private Long categoryId;
        private List<RestaurantMenuDto.MenuInfo> restaurantMenus;
        private List<ReviewDto.ReviewInfo> reviews;

        // 나머지 연관 관계도 추가해야 함...
    }

    // 식당 등록에 필요한 정보
    @Getter
    @Builder
    public static class RestaurantWriteRequest{
        private String name;
        private String details;
        private Double averageRate;
        private String imageUrl;
        private String contact;
        private String address;
        private Double lat;
        private Double lng;
        private Boolean runningState;
        private String summarizedReview;
        private Long categoryId;
    }

    @Getter
    @Builder
    public static class RestaurantWriteResponse{
        private String name;
    }

    // 모든 식당 조회
    @Getter
    @Builder
    public static class RestaurantShowAllResponse {
        private List<RestaurantInfo> restaurants;
    }

    @Getter
    @Builder
    public static class RestaurantShowAllReviewsResponse {
        private List<ReviewDto.ReviewInfo> reviews;
    }

    @Getter
    @Builder
    public static class RestaurantShowAllMenusResponse {
        private List<RestaurantMenuDto.MenuInfo> menus;
    }

    @Getter
    @Builder
    public static class RestaurantCategoryWriteResponse{
        private String restaurantName;
        private String categoryName;
    }

    @Getter
    @Builder
    public static class RestaurantShowCategoryResponse{
        private String categoryName;
    }
}
