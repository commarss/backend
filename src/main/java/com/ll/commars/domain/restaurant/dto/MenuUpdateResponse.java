package com.ll.commars.domain.restaurant.dto;

import com.ll.commars.domain.restaurant.entity.RestaurantMenu;

public record MenuUpdateResponse(
	Long id // 메뉴 ID
) {

	public static MenuUpdateResponse from(RestaurantMenu restaurantMenu) {
		return new MenuUpdateResponse(
			restaurantMenu.getId()
		);
	}
}
