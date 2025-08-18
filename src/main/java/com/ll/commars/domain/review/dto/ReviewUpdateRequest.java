package com.ll.commars.domain.review.dto;

public record ReviewUpdateRequest(
	Long userId,
	String title,
	String body,
	Integer rate
) {
}
