package com.ll.commars.domain.restaurant.menu.dto;

import java.util.List;

import com.ll.commars.domain.restaurant.entity.Menu;

public record MenuFindListResponse(
	List<MenuFindResponse> menus
) {

	public static MenuFindListResponse from(List<Menu> menus) {
		return new MenuFindListResponse(
			menus.stream()
				.map(MenuFindResponse::from)
				.toList()
		);
	}
}
