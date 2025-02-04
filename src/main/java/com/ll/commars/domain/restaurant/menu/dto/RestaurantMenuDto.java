package com.ll.commars.domain.restaurant.menu.dto;

import com.ll.commars.domain.restaurant.menu.entity.RestaurantMenu;
import lombok.Builder;
import lombok.Getter;

public class RestaurantMenuDto {
    @Getter
    @Builder
    public static class RestaurantMenuWriteRequest{
        private String restaurantName;
        private String name;
        private Integer price;
        private String imageUrl;
    }

    @Getter
    @Builder
    public static class RestaurantMenuWriteResponse{
        private String restaurantName;
        private String name;
        private Integer price;
    }

    @Getter
    @Builder
    public static class MenuInfo {
        private String name;
        private Integer price;
        private String imageUrl;
    }
}
