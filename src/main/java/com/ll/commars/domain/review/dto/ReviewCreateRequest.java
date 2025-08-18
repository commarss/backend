package com.ll.commars.domain.review.dto;

public record ReviewCreateRequest(
	Long userId,
	String title,
	String body,
	Integer rate
) {
}
