package com.ll.commars.domain.restaurant.menu.dto;

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

    // 메뉴 등록 및 수정 시 응답
    @Getter
    @Builder
    public static class MenuWriteResponse {
        private String restaurantName;
        private String name;
        private Integer price;
    }

    // 식당의 모든 메뉴 조회 시 응답
    @Getter
    @Builder
    public static class ShowAllMenusResponse {
        private List<MenuInfo> menus;
    }
}
