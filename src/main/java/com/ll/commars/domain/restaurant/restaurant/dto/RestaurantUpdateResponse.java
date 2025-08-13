package com.ll.commars.domain.restaurant.restaurant.dto;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;

public record RestaurantUpdateResponse(
	Long id // 식당 ID
) {

	public static RestaurantUpdateResponse from(Restaurant restaurant) {
		return new RestaurantUpdateResponse(
			restaurant.getId()
		);
	}
}
