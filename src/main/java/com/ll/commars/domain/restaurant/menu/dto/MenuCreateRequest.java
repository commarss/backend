package com.ll.commars.domain.restaurant.menu.dto;

public record MenuCreateRequest(
	String menuName,
	String imageUrl,
	Integer price
) {
}
