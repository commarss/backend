package com.ll.commars.domain.restaurant.menu.dto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.ll.commars.domain.restaurant.menu.entity.Menu;

public record MenuBulkCreateResponse(
	int createdCount,
	List<Long> createdMenuIds
) {

	public static MenuBulkCreateResponse from(List<Menu> savedMenus) {
		List<Long> ids = Optional.ofNullable(savedMenus)
			.orElse(Collections.emptyList())
			.stream()
			.map(Menu::getId)
			.toList();

		return new MenuBulkCreateResponse(ids.size(), ids);
	}
}
