package com.ll.commars.domain.restaurant.dto;

import com.ll.commars.domain.restaurant.entity.RestaurantMenu;

public record RestaurantMenuResponse(
	Long id,
	String name,
	Integer price,
	String imageUrl
) {

	public static RestaurantMenuResponse from(RestaurantMenu restaurantMenu) {
		return new RestaurantMenuResponse(
			restaurantMenu.getId(),
			restaurantMenu.getName(),
			restaurantMenu.getPrice(),
			restaurantMenu.getImageUrl()
		);
	}
}
