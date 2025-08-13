package com.ll.commars.domain.restaurant.businessHour.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.ll.commars.domain.restaurant.businessHour.dto.validation.ValidBusinessHour;

import jakarta.validation.constraints.NotNull;

@ValidBusinessHour
public record BusinessHourUpdateRequest(
	@NotNull(message = "요일을 입력해주세요.")
	DayOfWeek dayOfWeek,

	@NotNull(message = "오픈 시간을 입력해주세요.")
	LocalTime openTime,

	@NotNull(message = "마감 시간을 입력해주세요.")
	LocalTime closeTime
) {
}
