package com.ll.commars.domain.restaurant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.restaurant.dto.MenuCreateRequest;
import com.ll.commars.domain.restaurant.dto.MenuCreateResponse;
import com.ll.commars.domain.restaurant.dto.RestaurantCategoryDto;
import com.ll.commars.domain.restaurant.dto.RestaurantCreateRequest;
import com.ll.commars.domain.restaurant.dto.RestaurantCreateResponse;
import com.ll.commars.domain.restaurant.dto.RestaurantFindListResponse;
import com.ll.commars.domain.restaurant.dto.RestaurantFindResponse;
import com.ll.commars.domain.restaurant.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.dto.RestaurantUpdateRequest;
import com.ll.commars.domain.restaurant.dto.RestaurantUpdateResponse;
import com.ll.commars.domain.restaurant.service.RestaurantCommandService;
import com.ll.commars.domain.restaurant.service.RestaurantMenuService;
import com.ll.commars.domain.restaurant.service.RestaurantQueryService;
import com.ll.commars.domain.review.dto.ReviewDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

	@PostMapping("/{restaurant_id}/review")
	public ResponseEntity<ReviewDto.ReviewWriteResponse> writeReview(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable("restaurant_id") @NotNull Long restaurantId,
		@RequestBody @Valid ReviewDto.ReviewWriteRequest request
	) {
		Long userId = Long.valueOf(userDetails.getUsername());
		ReviewDto.ReviewWriteResponse response = restaurantCommandService.writeReview(restaurantId, request, userId);
		return ResponseEntity
			.status(201)
			.body(response);
	}

	@GetMapping("/{restaurant_id}/review")
	public ResponseEntity<ReviewDto.ShowAllReviewsResponse> getReviews(
		@PathVariable("restaurant_id") @NotNull Long restaurantId
	) {
		ReviewDto.ShowAllReviewsResponse response = restaurantCommandService.getReviews(restaurantId);
		return ResponseEntity
			.status(200)
			.body(response);
	}

	@GetMapping("/{restaurant_id}/category")
	public ResponseEntity<RestaurantCategoryDto.ShowCategoryNameResponse> getCategories(
		@PathVariable("restaurant_id") @NotNull Long restaurantId
	) {
		RestaurantCategoryDto.ShowCategoryNameResponse response = restaurantCommandService.getCategories(restaurantId);
		return ResponseEntity
			.status(200)
			.body(response);
	}
}
