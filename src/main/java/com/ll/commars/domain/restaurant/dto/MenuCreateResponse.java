package com.ll.commars.domain.restaurant.dto;

import com.ll.commars.domain.restaurant.entity.RestaurantMenu;

public record MenuCreateResponse(
	Long id // 메뉴 ID
) {

	public static MenuCreateResponse from(RestaurantMenu restaurantMenu) {
		return new MenuCreateResponse(
			restaurantMenu.getId()
		);
	}
}
