package com.ll.commars.domain.restaurant.menu.dto;

import java.util.List;

public record MenuBulkCreateRequest(
	List<MenuCreateRequest> menuCreateRequests
) {
}
