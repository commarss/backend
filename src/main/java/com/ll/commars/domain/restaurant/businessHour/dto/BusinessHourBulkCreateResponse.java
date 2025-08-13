package com.ll.commars.domain.restaurant.businessHour.dto;

public record BusinessHourBulkCreateResponse(
	long restaurantId,
	int createdCount
) {
}
