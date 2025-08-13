package com.ll.commars.domain.restaurant.restaurant.dto;

import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantCategory;
import com.querydsl.core.annotations.QueryProjection;

public record RestaurantSummaryResponse(
	Long id,
	String name,
	String imageUrl,
	Double averageRate,
	String address,
	String categoryName
) {

	@QueryProjection
	public RestaurantSummaryResponse(Long id, String name, String imageUrl, Double averageRate, String address, RestaurantCategory category) {
		this(
			id,
			name,
			imageUrl,
			averageRate,
			address,
			category != null ? category.name() : null
		);
	}
}
