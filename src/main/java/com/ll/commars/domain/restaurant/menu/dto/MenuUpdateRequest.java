package com.ll.commars.domain.restaurant.menu.dto;

import jakarta.validation.constraints.NotBlank;

public record MenuUpdateRequest(

	@NotBlank(message = "메뉴 이름은 필수입니다.")
	String menuName,
	String imageUrl,

	@NotBlank(message = "메뉴 가격은 필수입니다.")
	Integer price
) {
}
