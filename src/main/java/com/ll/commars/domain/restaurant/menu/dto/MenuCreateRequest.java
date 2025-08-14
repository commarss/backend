package com.ll.commars.domain.restaurant.menu.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MenuCreateRequest(

	@NotBlank(message = "메뉴 이름은 필수입니다.")
	String name,

	@URL
	String imageUrl,

	@NotNull(message = "메뉴 가격은 필수입니다.")
	Integer price
) {
}
