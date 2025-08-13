package com.ll.commars.domain.restaurant.businessHour.dto;

import com.ll.commars.domain.restaurant.businessHour.entity.BusinessHour;

public record BusinessHourUpdateResponse(
	Long id
) {

	public static BusinessHourUpdateResponse from(BusinessHour businessHour) {
		return new BusinessHourUpdateResponse(
			businessHour.getId()
		);
	}
}
