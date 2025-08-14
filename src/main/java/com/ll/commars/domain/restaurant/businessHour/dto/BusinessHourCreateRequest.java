package com.ll.commars.domain.restaurant.businessHour.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.ll.commars.domain.restaurant.businessHour.dto.validation.ValidBusinessHour;

import jakarta.validation.constraints.NotNull;

// todo: 개별 BusinessHour 에 대한 검증 추가(예: 같은 요일 불가능)
@ValidBusinessHour
public record BusinessHourCreateRequest(
	@NotNull(message = "요일을 입력해주세요.")
	DayOfWeek dayOfWeek,

	@NotNull(message = "오픈 시간을 입력해주세요.")
	LocalTime openTime,

	@NotNull(message = "마감 시간을 입력해주세요.")
	LocalTime closeTime
) implements BusinessHourData {
}
