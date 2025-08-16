package com.ll.commars.domain.restaurant.restaurant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.restaurant.restaurant.dto.CategoryFindListResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.CategoryFindResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantCreateRequest;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantCreateResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantFindListResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantFindResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantNearByRequest;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantSearchRequest;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantSearchResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantUpdateRequest;
import com.ll.commars.domain.restaurant.restaurant.service.RestaurantCommandService;
import com.ll.commars.domain.restaurant.restaurant.service.RestaurantDocService;
import com.ll.commars.domain.restaurant.restaurant.service.RestaurantQueryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

	private final RestaurantCommandService restaurantCommandService;
	private final RestaurantQueryService restaurantQueryService;
	private final RestaurantDocService restaurantDocService;

	@PostMapping
	public ResponseEntity<RestaurantCreateResponse> createRestaurant(
		@RequestBody @Valid RestaurantCreateRequest request
	) {
		RestaurantCreateResponse response = restaurantCommandService.createRestaurant(request);

		return ResponseEntity.ok(response);
	}

	// todo: 추후 페이징 적용
	@GetMapping
	public ResponseEntity<RestaurantFindListResponse> getRestaurants(
		@RequestParam(value = "category", required = false) String category
	) {
		RestaurantFindListResponse response;

		if (category != null && !category.trim().isEmpty()) {
			response = restaurantQueryService.getRestaurantsByCategory(category);
		} else {
			response = restaurantQueryService.getRestaurants();
		}

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
	public ResponseEntity<Void> updateRestaurant(
		@PathVariable("restaurant-id") Long restaurantId,
		@RequestBody @Valid RestaurantUpdateRequest request
	) {
		restaurantCommandService.updateRestaurant(restaurantId, request);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{restaurant-id}")
	public ResponseEntity<Void> deleteRestaurant(
		@PathVariable("restaurant-id") Long restaurantId
	) {
		restaurantCommandService.deleteRestaurant(restaurantId);

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

	@GetMapping("/search")
	public ResponseEntity<RestaurantSearchResponse> searchRestaurants(
		@Valid @ModelAttribute RestaurantSearchRequest request
	) {
		RestaurantSearchResponse response = restaurantDocService.searchRestaurants(request);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/sort/rate")
	public ResponseEntity<RestaurantSearchResponse> sortByRate() {
		RestaurantSearchResponse response = restaurantDocService.sortByRate();

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/nearby")
	public ResponseEntity<RestaurantSearchResponse> getNearbyRestaurants(
		@Valid @ModelAttribute RestaurantNearByRequest request
	) {
		RestaurantSearchResponse response = restaurantDocService.getNearbyRestaurants(request);

		return ResponseEntity.ok().body(response);
	}
}
