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

import com.ll.commars.domain.restaurant.menu.dto.MenuBulkCreateRequest;
import com.ll.commars.domain.restaurant.menu.dto.MenuBulkCreateResponse;
import com.ll.commars.domain.restaurant.menu.dto.MenuFindResponse;
import com.ll.commars.domain.restaurant.menu.dto.MenuUpdateRequest;
import com.ll.commars.domain.restaurant.menu.service.MenuService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {

	private final MenuService menuService;

	@PostMapping
	public ResponseEntity<MenuBulkCreateResponse> createMenu(
		@RequestBody @Valid MenuBulkCreateRequest request
	) {
		MenuBulkCreateResponse response = menuService.createMenu(request);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/{menu-id}")
	public ResponseEntity<MenuFindResponse> getMenu(
		@PathVariable("menu-id") Long menuId
	) {
		MenuFindResponse response = menuService.getMenu(menuId);

		return ResponseEntity.ok().body(response);
	}

	@PatchMapping("/{menu-id}")
	public ResponseEntity<Void> updateMenu(
		@PathVariable("menu-id") Long menuId,
		@RequestBody @Valid MenuUpdateRequest request
	) {
		menuService.updateMenu(menuId, request);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{menu-id}")
	public ResponseEntity<Void> deleteMenu(
		@PathVariable("menu-id") Long menuId
	) {
		menuService.deleteMenu(menuId);

		return ResponseEntity.ok().build();
	}
}
