package com.ll.commars.domain.user.favorite.dto;

import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class FavoriteDto {
    @Getter
    @Builder
    public static class FavoriteInfo {
        private Long id; // 찜 리스트 ID
        private String name; // 찜 리스트 이름
        private Boolean isPublic; // 찜 리스트 공개 여부
        private List<RestaurantDto.RestaurantInfo> restaurantLists;
    }

    @Getter
    @Builder
    public static class FavoriteResponse {
        private Long id;
        private List<FavoriteRestaurantResponse> favoriteRestaurants;
    }
}
