package com.ll.commars.domain.user.favorite.dto;

import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantDto;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class FavoriteDto {
    // 찜 리스트 정보
    @Getter
    @Builder
    public static class FavoriteInfo {
        private Long id; // 찜 리스트 ID
        private String name; // 찜 리스트 이름
        private Boolean isPublic; // 찜 리스트 공개 여부
        private List<RestaurantDto.RestaurantBasicInfo> restaurantLists;
    }

    // 찜 리스트에 속한 식당 응답
    @Getter
    @Builder
    public static class FavoriteResponse {
        private List<RestaurantDto.RestaurantInfo> favoriteRestaurants;
    }

    // 찜 리스트 생성 시 요청 (식당 추가 X)
    @Getter
    @Builder
    public static class CreateFavoriteListRequest{
        private String name;
        private Boolean isPublic;
    }

    // 찜 리스트에 식당 추가 시 요청
    @Getter
    @Builder
    public static class AddRestaurantRequest {
        private Long restaurantId;
    }
}
