package com.ll.commars.domain.review.dto;

public record ReviewCreateRequest(
	Long userId,
	Long restaurantId,
	String title,
	String body,
	Integer rate
) {
}
