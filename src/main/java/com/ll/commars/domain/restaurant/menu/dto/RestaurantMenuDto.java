package com.ll.commars.domain.restaurant.menu.dto;

import com.ll.commars.domain.restaurant.menu.entity.RestaurantMenu;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class RestaurantMenuDto {
    // 메뉴 정보
    @Getter
    @Builder
    public static class MenuInfo {
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
    public static class RestaurantMenuModifyResponse{
        private String reviewName;
        private String body;
        private Integer rate;
    }
}
