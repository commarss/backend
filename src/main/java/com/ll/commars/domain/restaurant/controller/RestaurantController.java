package com.ll.commars.domain.restaurant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.restaurant.dto.CategoryFindListResponse;
import com.ll.commars.domain.restaurant.dto.CategoryFindResponse;
import com.ll.commars.domain.restaurant.dto.MenuCreateRequest;
import com.ll.commars.domain.restaurant.dto.MenuCreateResponse;
import com.ll.commars.domain.restaurant.dto.MenuUpdateRequest;
import com.ll.commars.domain.restaurant.dto.MenuUpdateResponse;
import com.ll.commars.domain.restaurant.dto.RestaurantCreateRequest;
import com.ll.commars.domain.restaurant.dto.RestaurantCreateResponse;
import com.ll.commars.domain.restaurant.dto.RestaurantFindListResponse;
import com.ll.commars.domain.restaurant.dto.RestaurantFindResponse;
import com.ll.commars.domain.restaurant.dto.RestaurantUpdateRequest;
import com.ll.commars.domain.restaurant.dto.RestaurantUpdateResponse;
import com.ll.commars.domain.restaurant.service.RestaurantCommandService;
import com.ll.commars.domain.restaurant.service.RestaurantMenuService;
import com.ll.commars.domain.restaurant.service.RestaurantQueryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
public class RestaurantController {

	private final RestaurantCommandService restaurantCommandService;
	private final RestaurantQueryService restaurantQueryService;
	private final RestaurantMenuService restaurantMenuService;

	@PostMapping
	public ResponseEntity<RestaurantCreateResponse> createRestaurant(
		@RequestBody @Valid RestaurantCreateRequest request
	) {
		RestaurantCreateResponse response = restaurantCommandService.createRestaurant(request);

		return ResponseEntity.ok(response);
	}

	// todo: 추후 페이징 적용
	@GetMapping
	public ResponseEntity<RestaurantFindListResponse> getRestaurants() {
		RestaurantFindListResponse response = restaurantQueryService.getRestaurants();

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/{restaurant-id}")
	public ResponseEntity<RestaurantFindResponse> getRestaurant(
		@PathVariable("restaurant-id") Long restaurantId
	) {
		RestaurantFindResponse response = restaurantQueryService.getRestaurant(restaurantId);

		return ResponseEntity.ok().body(response);
	}

	// todo: 추후 메뉴, 영업시간 등 다양한 정보를 수정할 수 있도록
	@PatchMapping("/{restaurant-id}")
	public ResponseEntity<RestaurantUpdateResponse> updateRestaurant(
		@PathVariable("restaurant-id") Long restaurantId,
		@RequestBody @Valid RestaurantUpdateRequest request
	) {
		RestaurantUpdateResponse response = restaurantCommandService.updateRestaurant(restaurantId, request);

		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping("/{restaurant-id}")
	public ResponseEntity<Void> deleteRestaurant(
		@PathVariable("restaurant-id") Long restaurantId
	) {
		restaurantCommandService.deleteRestaurant(restaurantId);

		return ResponseEntity.ok().build();
	}

	@PostMapping("{restaurant-id}/menu")
	public ResponseEntity<MenuCreateResponse> createMenu(
		@PathVariable("restaurant-id") Long restaurantId,
		@RequestBody @Valid MenuCreateRequest request
	) {
		MenuCreateResponse response = restaurantMenuService.createMenu(restaurantId, request);

		return ResponseEntity.ok().body(response);
	}

	// todo: 별도의 menu controller를 구현해야 할지
	@PatchMapping("{restaurant-id}/menu/{menu-id}")
	public ResponseEntity<MenuUpdateResponse> updateMenu(
		@PathVariable("menu-id") Long menuId,
		@RequestBody @Valid MenuUpdateRequest request
	) {
		MenuUpdateResponse response = restaurantMenuService.updateMenu(menuId, request);

		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping("{restaurant-id}/menu/{menu-id}")
	public ResponseEntity<Void> deleteMenu(
		@PathVariable("menu-id") Long menuId
	) {
		restaurantMenuService.deleteMenu(menuId);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/{restaurant-id}/category")
	public ResponseEntity<CategoryFindResponse> getCategoryFromRestaurant(
		@PathVariable("restaurant-id") Long restaurantId
	) {
		CategoryFindResponse response = restaurantQueryService.getCategoryFromRestaurant(restaurantId);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/category")
	public ResponseEntity<CategoryFindListResponse> getAllCategories() {
		CategoryFindListResponse response = restaurantQueryService.getAllCategories();

		return ResponseEntity.ok().body(response);
	}
}
