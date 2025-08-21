package com.ll.commars.domain.favorite.dto;

import jakarta.validation.constraints.NotNull;

public record FavoriteRestaurantCreateRequest(
    @NotNull(message = "찜 리스트 ID는 필수입니다.")
    Long favoriteId
) {
}
