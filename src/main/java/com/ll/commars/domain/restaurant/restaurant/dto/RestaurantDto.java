package com.ll.commars.domain.restaurant.restaurant.dto;

import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourDto;
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
    // 식당 전체 정보
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
        private List<BusinessHourDto.BusinessHourInfo> businessHours;

        // 나머지 연관 관계도 추가해야 함...
    }

    // 식당 기본 정보 (연관관계 제외)
    @Getter
    @Builder
    public static class RestaurantBasicInfo {
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
    }

    // 식당 등록 및 수정에 필요한 기본 정보
    // 메뉴, 영업시간 등은 따로 추가
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

    // 식당 등록 및 수정 응답
    @Getter
    @Builder
    public static class RestaurantWriteResponse{
        private Long id;
        private String name;
    }

    // 모든 식당 조회
    @Getter
    @Builder
    public static class RestaurantShowAllResponse {
        private List<RestaurantInfo> restaurants;
    }

    // 식당 카테고리 등록 및 수정 시의 응답
    @Getter
    @Builder
    public static class RestaurantCategoryWriteResponse{
        private String restaurantName;
        private String categoryName;
    }
}
