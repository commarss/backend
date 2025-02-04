package com.ll.commars.domain.restaurant.menu.dto;

import com.ll.commars.domain.restaurant.menu.entity.RestaurantMenu;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestaurantMenuDTO {
    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;
    private Long restaurantId;

    public static RestaurantMenuDTO from(RestaurantMenu menu) {
        return RestaurantMenuDTO.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .imageUrl(menu.getImageUrl())
                .restaurantId(menu.getRestaurant().getId())
                .build();
    }
}
