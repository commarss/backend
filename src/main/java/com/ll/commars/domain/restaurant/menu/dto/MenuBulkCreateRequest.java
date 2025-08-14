package com.ll.commars.domain.restaurant.menu.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record MenuBulkCreateRequest(
	Long restaurantId,

	@NotEmpty(message = "등록할 메뉴가 최소 1개 존재해야 합니다.")
	@Valid
	List<MenuCreateRequest> menuCreateRequests
) {
}
