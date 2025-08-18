package com.ll.commars.domain.review.dto;

import com.ll.commars.domain.review.entity.Review;

public record ReviewFindResponse(
	Long id, // 리뷰 ID
	Long userId,
	String title,
	String body,
	Integer rate
) {

	public static ReviewFindResponse from(Review review) {
		return new ReviewFindResponse(
			review.getId(),
			review.getMember().getId(),
			review.getTitle(),
			review.getBody(),
			review.getRate()
		);
	}
}
