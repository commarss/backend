package com.ll.commars.domain.restaurant.restaurant.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;

public record RestaurantCreateRequest(

	@NotBlank(message = "식당 이름은 필수입니다.")
	String name,

	@NotBlank(message = "식당 설명은 필수입니다.")
	String details,

	@URL
	String imageUrl,
	String contact,

	@NotBlank(message = "식당 주소는 필수입니다.")
	String address,

	@NotBlank(message = "식당 카테고리는 필수입니다.")
	String category
	) {
}
