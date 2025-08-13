package com.ll.commars.domain.restaurant.dto;

import java.util.Optional;

import com.ll.commars.domain.restaurant.entity.Restaurant;
import com.ll.commars.domain.review.dto.RestaurantReviewListResponse;

public record RestaurantFindResponse(
	Long id,
	String name,
	String details,
	Double averageRate,
	String imageUrl,
	String contact,
	String address,
	Boolean runningState,
	String summarizedReview,
	String categoryName,
	MenuFindListResponse restaurantMenus,
	RestaurantReviewListResponse reviews,
	BusinessHourListResponse businessHours
) {

	public static RestaurantFindResponse from(Restaurant restaurant) {
		return new RestaurantFindResponse(
			restaurant.getId(),
			restaurant.getName(),
			restaurant.getDetails(),
			restaurant.getAverageRate(),
			restaurant.getImageUrl(),
			restaurant.getContact(),
			restaurant.getAddress(),
			restaurant.getRunningState(),
			restaurant.getSummarizedReview(),
			Optional.ofNullable(restaurant.getRestaurantCategory()).map(Enum::name).orElse(null),
			MenuFindListResponse.from(restaurant.getMenus()),
			RestaurantReviewListResponse.from(restaurant.getReviews()),
			BusinessHourListResponse.from(restaurant.getBusinessHours())
		);
	}
}
