package com.ll.commars.domain.restaurant.dto;

import java.util.List;

import com.ll.commars.domain.restaurant.entity.RestaurantMenu;

public record RestaurantMenuListResponse(
	List<RestaurantMenuResponse> menus
) {

	public static RestaurantMenuListResponse from(List<RestaurantMenu> menus) {
		return new RestaurantMenuListResponse(
			menus.stream()
				.map(RestaurantMenuResponse::from)
				.toList()
		);
	}
}
