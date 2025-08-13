package com.ll.commars.domain.restaurant.businessHour.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record BusinessHourBulkCreateRequest(
	@NotEmpty(message = "등록할 영업시간 정보가 하나 이상 있어야 합니다.")
	@Size(max = 7, message = "영업시간 정보는 최대 7개(요일별)까지 등록할 수 있습니다.")
	@Valid
	List<BusinessHourCreateRequest> businessHours
) {
}
