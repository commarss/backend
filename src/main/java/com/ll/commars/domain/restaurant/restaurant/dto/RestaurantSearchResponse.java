package com.ll.commars.domain.restaurant.restaurant.dto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantDoc;

public record RestaurantSearchResponse(
	List<RestaurantSearchInfo> restaurants,
	int totalCount
) {

	public static RestaurantSearchResponse from(List<RestaurantDoc> restaurantDocs) {
		if (restaurantDocs == null || restaurantDocs.isEmpty()) {
			return new RestaurantSearchResponse(Collections.emptyList(), 0);
		}

		List<RestaurantSearchInfo> restaurants = restaurantDocs.stream()
			.filter(Objects::nonNull)
			.map(RestaurantSearchResponse::mapToSearchInfo)
			.toList();

		return new RestaurantSearchResponse(restaurants, restaurants.size());
	}

	private static RestaurantSearchInfo mapToSearchInfo(RestaurantDoc doc) {
		return new RestaurantSearchInfo(
			Optional.ofNullable(doc.getName()).orElse("이름 없음"),
			Optional.ofNullable(doc.getAddress()).orElse("주소 없음"),
			Optional.ofNullable(doc.getCategory()).orElse("미분류")
		);
	}

	public record RestaurantSearchInfo(
		String name,
		String address,
		String category
	) {
	}
}
