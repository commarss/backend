package com.ll.commars.domain.restaurant.dto;

public record MenuUpdateRequest(
	String menuName,
	String imageUrl,
	Integer price
) {
}
