package com.ll.commars.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewUpdateRequest(
	@NotBlank(message = "제목은 필수입니다.")
	String title,

	@NotBlank(message = "내용은 필수입니다.")
	String body,

	@Min(value = 1)
	@Max(value = 5)
	@NotNull
	Integer rate
) {
}
