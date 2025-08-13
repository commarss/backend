package com.ll.commars.domain.restaurant.menu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.restaurant.menu.dto.MenuCreateRequest;
import com.ll.commars.domain.restaurant.menu.dto.MenuCreateResponse;
import com.ll.commars.domain.restaurant.menu.dto.MenuFindResponse;
import com.ll.commars.domain.restaurant.menu.dto.MenuUpdateRequest;
import com.ll.commars.domain.restaurant.menu.dto.MenuUpdateResponse;
import com.ll.commars.domain.restaurant.menu.service.RestaurantMenuService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant/{restaurant-id}/menus")
public class MenuController {

	private final RestaurantMenuService restaurantMenuService;

	@PostMapping
	public ResponseEntity<MenuCreateResponse> createMenu(
		@PathVariable("restaurant-id") Long restaurantId,
		@RequestBody @Valid MenuCreateRequest request
	) {
		MenuCreateResponse response = restaurantMenuService.createMenu(restaurantId, request);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/{menu-id}")
	public ResponseEntity<MenuFindResponse> getMenu(
		@PathVariable("menu-id") Long menuId
	) {
		MenuFindResponse response = restaurantMenuService.getMenu(menuId);

		return ResponseEntity.ok().body(response);
	}

	@PatchMapping("/{menu-id}")
	public ResponseEntity<MenuUpdateResponse> updateMenu(
		@PathVariable("menu-id") Long menuId,
		@RequestBody @Valid MenuUpdateRequest request
	) {
		MenuUpdateResponse response = restaurantMenuService.updateMenu(menuId, request);

		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping("/{menu-id}")
	public ResponseEntity<Void> deleteMenu(
		@PathVariable("menu-id") Long menuId
	) {
		restaurantMenuService.deleteMenu(menuId);

		return ResponseEntity.ok().build();
	}
}
