package com.ll.commars.domain.restaurant.restaurant.dto;

import com.ll.commars.domain.restaurant.menu.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.menu.entity.RestaurantMenu;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class RestaurantDto {
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
    }

    @Getter
    @Builder
    public static class RestaurantWriteResponse{
        private String name;
    }

    // 식당 이름과 식당의 메뉴 리스트
    @Getter
    @Builder
    public static class RestaurantInfo {
        private String restaurantName;
        private List<RestaurantMenuDto.MenuInfo> restaurantMenus;
    }

    @Getter
    @Builder
    public static class RestaurantShowAllResponse {
        private List<RestaurantInfo> restaurants;
    }
}
