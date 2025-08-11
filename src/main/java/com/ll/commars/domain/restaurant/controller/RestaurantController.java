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

import com.ll.commars.domain.restaurant.dto.BusinessHourDto;
import com.ll.commars.domain.restaurant.dto.RestaurantCategoryDto;
import com.ll.commars.domain.restaurant.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.service.RestaurantMenuService;
import com.ll.commars.domain.restaurant.dto.RestaurantDto;
import com.ll.commars.domain.restaurant.service.RestaurantService;
import com.ll.commars.domain.review.dto.ReviewDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
public class RestaurantController {

	private final RestaurantService restaurantService;
	private final RestaurantMenuService restaurantMenuService;

	@PostMapping("/")
	public ResponseEntity<RestaurantDto.RestaurantWriteResponse> write(
		@RequestBody @Valid RestaurantDto.RestaurantWriteRequest request
	) {
		RestaurantDto.RestaurantWriteResponse response = restaurantService.write(request);
		return ResponseEntity
			.status(201)
			.body(response);
	}

	@GetMapping("/")
	public ResponseEntity<RestaurantDto.RestaurantShowAllResponse> getRestaurants() {
		RestaurantDto.RestaurantShowAllResponse response = restaurantService.getRestaurants();
		return ResponseEntity
			.status(200)
			.body(response);
	}

	@GetMapping("/{restaurant_id}")
	public ResponseEntity<RestaurantDto.RestaurantInfo> getRestaurant(
		@PathVariable("restaurant_id") @NotNull Long restaurantId
	) {
		RestaurantDto.RestaurantInfo response = restaurantService.getRestaurant(restaurantId);
		return ResponseEntity
			.status(200)
			.body(response);
	}

	@PatchMapping("/{restaurant_id}")
	public ResponseEntity<RestaurantDto.RestaurantWriteResponse> modifyRestaurant(
		@PathVariable("restaurant_id") @NotNull Long restaurantId,
		@RequestBody @Valid RestaurantDto.RestaurantWriteRequest request
	) {
		RestaurantDto.RestaurantWriteResponse response = restaurantService.modifyRestaurant(restaurantId, request);
		return ResponseEntity
			.status(200)
			.body(response);
	}

	@DeleteMapping("/{restaurant_id}")
	public ResponseEntity<String> deleteRestaurant(
		@PathVariable("restaurant_id") @NotNull Long restaurantId
	) {
		restaurantService.deleteRestaurant(restaurantId);
		return ResponseEntity
			.status(200)
			.body("식당 삭제 성공");
	}

	@PostMapping("{restaurant_id}/menu")
	public ResponseEntity<RestaurantMenuDto.MenuWriteResponse> writeMenu(
		@PathVariable("restaurant_id") @NotNull Long restaurantId,
		@RequestBody @Valid RestaurantMenuDto.MenuInfo request
	) {
		RestaurantMenuDto.MenuWriteResponse response = restaurantMenuService.write(restaurantId, request);

		return ResponseEntity
			.status(201)
			.body(response);
	}

	@GetMapping("/{restaurant_id}/menu")
	public ResponseEntity<RestaurantMenuDto.ShowAllMenusResponse> getMenus(
		@PathVariable("restaurant_id") @NotNull Long restaurantId
	) {
		RestaurantMenuDto.ShowAllMenusResponse response = restaurantService.getMenus(restaurantId);
		return ResponseEntity
			.status(200)
			.body(response);
	}

	@PostMapping("/{restaurant_id}/review")
	public ResponseEntity<ReviewDto.ReviewWriteResponse> writeReview(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable("restaurant_id") @NotNull Long restaurantId,
		@RequestBody @Valid ReviewDto.ReviewWriteRequest request
	) {
		Long userId = Long.valueOf(userDetails.getUsername());
		ReviewDto.ReviewWriteResponse response = restaurantService.writeReview(restaurantId, request, userId);
		return ResponseEntity
			.status(201)
			.body(response);
	}

	@GetMapping("/{restaurant_id}/review")
	public ResponseEntity<ReviewDto.ShowAllReviewsResponse> getReviews(
		@PathVariable("restaurant_id") @NotNull Long restaurantId
	) {
		ReviewDto.ShowAllReviewsResponse response = restaurantService.getReviews(restaurantId);
		return ResponseEntity
			.status(200)
			.body(response);
	}

	@PostMapping("/{restaurant_id}/category")
	public ResponseEntity<RestaurantDto.RestaurantCategoryWriteResponse> writeCategory(
		@PathVariable("restaurant_id") @NotNull Long restaurantId,
		@RequestBody @Valid RestaurantCategoryDto.RestaurantCategoryWriteRequest request
	) {
		RestaurantDto.RestaurantCategoryWriteResponse response = restaurantService.writeCategory(restaurantId, request);
		return ResponseEntity
			.status(201)
			.body(response);
	}

	@GetMapping("/{restaurant_id}/category")
	public ResponseEntity<RestaurantCategoryDto.ShowCategoryNameResponse> getCategories(
		@PathVariable("restaurant_id") @NotNull Long restaurantId
	) {
		RestaurantCategoryDto.ShowCategoryNameResponse response = restaurantService.getCategories(restaurantId);
		return ResponseEntity
			.status(200)
			.body(response);
	}

	@PatchMapping("/{restaurant_id}/category")
	public ResponseEntity<RestaurantDto.RestaurantCategoryWriteResponse> modifyCategory(
		@PathVariable("restaurant_id") @NotNull Long restaurantId,
		@RequestBody @Valid RestaurantCategoryDto.RestaurantCategoryWriteRequest request
	) {
		RestaurantDto.RestaurantCategoryWriteResponse response = restaurantService.modifyCategory(restaurantId,
			request);
		return ResponseEntity
			.status(200)
			.body(response);
	}

	@PostMapping("/{restaurant_id}/business-hour")
	public ResponseEntity<BusinessHourDto.BusinessHourWriteResponse> writeBusinessHours(
		@PathVariable("restaurant_id") @NotNull Long restaurantId,
		@RequestBody @Valid BusinessHourDto.BusinessHourWriteRequest request
	) {
		BusinessHourDto.BusinessHourWriteResponse response = restaurantService.writeBusinessHours(restaurantId,
			request);
		return ResponseEntity
			.status(201)
			.body(response);
	}

	@PatchMapping("/{restaurant_id}/business-hour")
	public ResponseEntity<BusinessHourDto.BusinessHourWriteResponse> modifyBusinessHours(
		@PathVariable("restaurant_id") @NotNull Long restaurantId,
		@RequestBody @Valid BusinessHourDto.BusinessHourWriteRequest request
	) {
		BusinessHourDto.BusinessHourWriteResponse response = restaurantService.modifyBusinessHours(restaurantId,
			request);
		return ResponseEntity
			.status(200)
			.body(response);
	}
}
