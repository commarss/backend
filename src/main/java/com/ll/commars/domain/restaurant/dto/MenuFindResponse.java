package com.ll.commars.domain.restaurant.dto;

import com.ll.commars.domain.restaurant.entity.RestaurantMenu;

public record MenuFindResponse(
	Long id,
	String name,
	Integer price,
	String imageUrl
) {

	public static MenuFindResponse from(RestaurantMenu restaurantMenu) {
		return new MenuFindResponse(
			restaurantMenu.getId(),
			restaurantMenu.getName(),
			restaurantMenu.getPrice(),
			restaurantMenu.getImageUrl()
		);
	}
}
