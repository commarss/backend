package com.ll.commars.domain.restaurant.businessHour.dto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.ll.commars.domain.restaurant.businessHour.entity.BusinessHour;

public record BusinessHourListResponse(
	List<BusinessHourResponse> businessHours
) {

	public static BusinessHourListResponse from(List<BusinessHour> businessHours) {
		List<BusinessHourResponse> businessHourResponses = Optional.ofNullable(businessHours)
			.orElse(Collections.emptyList())
			.stream()
			.map(BusinessHourResponse::from)
			.toList();

		return new BusinessHourListResponse(businessHourResponses);
	}
}
