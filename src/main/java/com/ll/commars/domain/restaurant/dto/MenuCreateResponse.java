package com.ll.commars.domain.restaurant.dto;

import com.ll.commars.domain.restaurant.entity.Menu;

public record MenuCreateResponse(
	Long id // 메뉴 ID
) {

	public static MenuCreateResponse from(Menu menu) {
		return new MenuCreateResponse(
			menu.getId()
		);
	}
}
