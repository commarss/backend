package com.ll.commars.domain.restaurant.dto;

public record RestaurantUpdateRequest(
	String restaurantName,
	String category
) {
}
