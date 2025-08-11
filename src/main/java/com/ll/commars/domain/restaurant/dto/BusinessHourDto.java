package com.ll.commars.domain.restaurant.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

public class BusinessHourDto {

	// 영업시간 정보(단건)
	@Getter
	@Builder
	public static class BusinessHourInfo {

		private Long id;
		private Integer dayOfWeek;
		private LocalDateTime openTime;
		private LocalDateTime closeTime;
	}

	// 영업시간 등록 및 수정 시 받을 정보
	@Getter
	@Builder
	public static class BusinessHourWriteInfo {

		private Integer dayOfWeek;
		private LocalDateTime openTime;
		private LocalDateTime closeTime;
	}

	// 영업시간 등록 및 수정 시 요청
	@Getter
	@Builder
	public static class BusinessHourWriteRequest {

		private List<BusinessHourWriteInfo> businessHours;
	}

	// 영업시간 등록 및 수정 시 응답
	@Getter
	@Builder
	public static class BusinessHourWriteResponse {

		private String restaurantName;
		private List<BusinessHourInfo> businessHours;
	}
}
