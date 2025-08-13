package com.ll.commars.domain.restaurant.businessHour.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.ll.commars.domain.restaurant.businessHour.entity.BusinessHour;

public record BusinessHourResponse(
	Long id,
	DayOfWeek dayOfWeek,
	LocalTime openTime,
	LocalTime closeTime
) {

	public static BusinessHourResponse from(BusinessHour businessHour) {
		return new BusinessHourResponse(
			businessHour.getId(),
			businessHour.getDayOfWeek(),
			businessHour.getOpenTime(),
			businessHour.getCloseTime()
		);
	}
}
