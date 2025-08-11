package com.ll.commars.domain.restaurant.dto;

import java.util.List;

import com.ll.commars.domain.restaurant.entity.BusinessHour;

public record BusinessHourListResponse(
	List<BusinessHourResponse> businessHours
) {

	public static BusinessHourListResponse from(List<BusinessHour> businessHours) {
		return new BusinessHourListResponse(
			businessHours.stream()
				.map(BusinessHourResponse::from)
				.toList()
		);
	}
}
