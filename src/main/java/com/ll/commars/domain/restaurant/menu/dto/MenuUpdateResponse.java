package com.ll.commars.domain.restaurant.menu.dto;

import com.ll.commars.domain.restaurant.entity.Menu;

public record MenuUpdateResponse(
	Long id // 메뉴 ID
) {

	public static MenuUpdateResponse from(Menu menu) {
		return new MenuUpdateResponse(
			menu.getId()
		);
	}
}
