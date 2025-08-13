package com.ll.commars.domain.restaurant.dto;

import com.ll.commars.domain.restaurant.entity.Restaurant;

public record RestaurantUpdateResponse(
	Long id // 식당 ID
) {

	public static RestaurantUpdateResponse from(Restaurant restaurant) {
		return new RestaurantUpdateResponse(
			restaurant.getId()
		);
	}
}
