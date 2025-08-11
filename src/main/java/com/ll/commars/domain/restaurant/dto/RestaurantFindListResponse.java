package com.ll.commars.domain.restaurant.dto;

import java.util.List;

public record RestaurantFindListResponse(
	List<RestaurantSummaryResponse> restaurants
) {
}
