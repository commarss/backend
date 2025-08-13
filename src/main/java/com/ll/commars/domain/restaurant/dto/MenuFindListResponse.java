package com.ll.commars.domain.restaurant.dto;

import java.util.List;

import com.ll.commars.domain.restaurant.entity.RestaurantMenu;

public record MenuFindListResponse(
	List<MenuFindResponse> menus
) {

	public static MenuFindListResponse from(List<RestaurantMenu> menus) {
		return new MenuFindListResponse(
			menus.stream()
				.map(MenuFindResponse::from)
				.toList()
		);
	}
}
