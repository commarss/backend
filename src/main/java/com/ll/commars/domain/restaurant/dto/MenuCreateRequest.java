package com.ll.commars.domain.restaurant.dto;

public record MenuCreateRequest(
	String menuName,
	String imageUrl,
	Integer price
) {
}
