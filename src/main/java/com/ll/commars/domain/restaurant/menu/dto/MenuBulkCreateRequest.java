package com.ll.commars.domain.restaurant.menu.dto;

import java.util.List;

public record MenuBulkCreateRequest(
	Long restaurantId,
	List<MenuCreateRequest> menuCreateRequests
) {
}
