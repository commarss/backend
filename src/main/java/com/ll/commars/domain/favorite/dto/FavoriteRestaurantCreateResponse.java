package com.ll.commars.domain.favorite.dto;

import com.ll.commars.domain.favorite.entity.FavoriteRestaurant;

public record FavoriteRestaurantCreateResponse(
    Long id // 찜한 음식점 ID
) {
    public static FavoriteRestaurantCreateResponse from(FavoriteRestaurant favoriteRestaurant) {
        return new FavoriteRestaurantCreateResponse(favoriteRestaurant.getId());
    }
}
