package com.ll.commars.domain.restaurant.menu.dto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.ll.commars.domain.restaurant.menu.entity.Menu;

public record MenuFindListResponse(
	List<MenuFindResponse> menus
) {

	public static MenuFindListResponse from(List<Menu> menus) {
		List<MenuFindResponse> menuResponses = Optional.ofNullable(menus)
			.orElse(Collections.emptyList())
			.stream()
			.map(MenuFindResponse::from)
			.toList();

		return new MenuFindListResponse(menuResponses);
	}
}
