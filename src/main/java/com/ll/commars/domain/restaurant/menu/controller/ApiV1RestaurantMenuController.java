package com.ll.commars.domain.restaurant.menu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.restaurant.menu.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.menu.service.RestaurantMenuService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menu")
public class ApiV1RestaurantMenuController {

	private final RestaurantMenuService restaurantMenuService;

	@PatchMapping("/{menu_id}")
	public ResponseEntity<RestaurantMenuDto.MenuWriteResponse> modifyMenu(
		@PathVariable("menu_id") @NotNull Long menuId,
		@RequestBody @Valid RestaurantMenuDto.MenuInfo request
	) {
		RestaurantMenuDto.MenuWriteResponse response = restaurantMenuService.modifyMenu(menuId, request);
		return ResponseEntity
			.status(200)
			.body(response);
	}

	@DeleteMapping("/{menu_id}")
	public ResponseEntity<String> deleteMenu(
		@PathVariable("menu_id") @NotNull Long menuId
	) {
		restaurantMenuService.deleteMenu(menuId);
		return ResponseEntity
			.status(200)
			.body("식당 메뉴 삭제 성공");
	}

	@GetMapping("/showAllMenus")
	public ResponseEntity<RestaurantMenuDto.ShowAllMenusResponse> showAllReviews(
		@RequestParam("restaurant_id") @NotNull Long restaurantId
	) {
		RestaurantMenuDto.ShowAllMenusResponse response = restaurantMenuService.findByRestaurantId(restaurantId);
		return ResponseEntity
			.status(200)
			.body(response);
	}
}
