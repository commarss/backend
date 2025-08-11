package com.ll.commars.domain.restaurant.dto;

import com.ll.commars.domain.restaurant.entity.Restaurant;

public record RestaurantCreateResponse(
	Long id, // 식당 ID
	String name
) {

	public static RestaurantCreateResponse from(Restaurant restaurant) {
		return new RestaurantCreateResponse(
			restaurant.getId(),
			restaurant.getName()
		);
	}
}
