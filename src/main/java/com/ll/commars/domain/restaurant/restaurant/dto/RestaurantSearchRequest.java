package com.ll.commars.domain.restaurant.restaurant.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RestaurantSearchRequest(
	@NotBlank(message = "검색어는 필수입니다.")
	String keyword,

	@NotNull(message = "위도는 필수입니다.")
	@DecimalMin(value = "-90", message = "위도는 -90 이상이어야 합니다.")
	@DecimalMax(value = "90", message = "위도는 90 이하여야 합니다.")
	Double lat,

	@NotNull(message = "경도는 필수입니다.")
	@DecimalMin(value = "-180", message = "경도는 -180 이상이어야 합니다.")
	@DecimalMax(value = "180", message = "경도는 180 이하여야 합니다.")
	Double lon,

	@Positive(message = "거리는 양수여야 합니다.")
	Double distance
) {
}
