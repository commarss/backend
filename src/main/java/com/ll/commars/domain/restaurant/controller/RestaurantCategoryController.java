package com.ll.commars.domain.restaurant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.restaurant.dto.RestaurantCategoryDto;
import com.ll.commars.domain.restaurant.service.RestaurantCategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant-category")
public class RestaurantCategoryController {

	private final RestaurantCategoryService restaurantCategoryService;

	@PostMapping("/")
	public ResponseEntity<RestaurantCategoryDto.RestaurantCategoryInfo> writeCategory(
		@RequestBody @Valid RestaurantCategoryDto.RestaurantCategoryEnrollRequest request
	) {
		RestaurantCategoryDto.RestaurantCategoryInfo response = restaurantCategoryService.writeCategory(request);
		return ResponseEntity
			.status(201)
			.body(response);
	}

	@GetMapping("/")
	public ResponseEntity<RestaurantCategoryDto.ShowAllCategoriesResponse> getCategories() {
		RestaurantCategoryDto.ShowAllCategoriesResponse response = restaurantCategoryService.getCategories();
		return ResponseEntity
			.status(200)
			.body(response);
	}

	@GetMapping("/{category_id}")
	public ResponseEntity<RestaurantCategoryDto.ShowAllRestaurantsByCategoryResponse> getRestaurantByCategory(
		@PathVariable("category_id") Long categoryId
	) {
		RestaurantCategoryDto.ShowAllRestaurantsByCategoryResponse response = restaurantCategoryService.getRestaurantByCategory(
			categoryId);

		return ResponseEntity
			.status(200)
			.body(response);
	}
}

