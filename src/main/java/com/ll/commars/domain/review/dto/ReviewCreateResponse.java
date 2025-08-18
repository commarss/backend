package com.ll.commars.domain.review.dto;

import com.ll.commars.domain.review.entity.Review;

public record ReviewCreateResponse(
	Long id, // 리뷰 ID
	String title,
	String body,
	Integer rate
) {

	public static ReviewCreateResponse from(Review review) {
		return new ReviewCreateResponse(
			review.getId(),
			review.getTitle(),
			review.getBody(),
			review.getRate()
		);
	}
}
