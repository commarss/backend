package com.ll.commars.domain.restaurant.restaurant.dto;

import java.util.List;

public record RestaurantFindListResponse(
	List<RestaurantSummaryResponse> restaurants
) {
}
