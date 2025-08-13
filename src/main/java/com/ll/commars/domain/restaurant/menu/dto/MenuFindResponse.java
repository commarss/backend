package com.ll.commars.domain.restaurant.menu.dto;

import com.ll.commars.domain.restaurant.menu.entity.Menu;

public record MenuFindResponse(
	Long id,
	String name,
	Integer price,
	String imageUrl
) {

	public static MenuFindResponse from(Menu menu) {
		return new MenuFindResponse(
			menu.getId(),
			menu.getName(),
			menu.getPrice(),
			menu.getImageUrl()
		);
	}
}
