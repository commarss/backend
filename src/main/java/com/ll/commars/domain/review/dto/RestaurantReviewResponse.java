package com.ll.commars.domain.review.dto;

import com.ll.commars.domain.review.entity.Review;

public record RestaurantReviewResponse(
	Long id,
	Long userId,
	String name,
	String body,
	Integer rate
) {

	public static RestaurantReviewResponse from(Review review) {
		return new RestaurantReviewResponse(
			review.getId(),
			review.getMember().getId(),
			review.getMember().getName(),
			review.getBody(),
			review.getRate()
		);
	}
}
