package com.ll.commars.domain.restaurant.menu.dto;

public record MenuUpdateRequest(
	String menuName,
	String imageUrl,
	Integer price
) {
}
