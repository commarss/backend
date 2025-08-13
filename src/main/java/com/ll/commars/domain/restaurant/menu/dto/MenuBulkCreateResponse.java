package com.ll.commars.domain.restaurant.menu.dto;

import java.util.List;

import com.ll.commars.domain.restaurant.entity.Menu;

public record MenuBulkCreateResponse(
	int createdCount,
	List<Long> createdMenuIds
) {

	public static MenuBulkCreateResponse from(List<Menu> savedMenus) {
		List<Long> ids = savedMenus.stream()
			.map(Menu::getId)
			.toList();
		return new MenuBulkCreateResponse(savedMenus.size(), ids);
	}
}
