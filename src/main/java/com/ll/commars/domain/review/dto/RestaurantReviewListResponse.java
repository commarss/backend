package com.ll.commars.domain.review.dto;

import java.util.List;

import com.ll.commars.domain.review.entity.Review;

public record RestaurantReviewListResponse(
	List<RestaurantReviewResponse> reviews
) {

	public static RestaurantReviewListResponse from(List<Review> reviews) {
		return new RestaurantReviewListResponse(
			reviews.stream()
				.map(RestaurantReviewResponse::from)
				.toList()
		);
	}
}
