package com.ll.commars.domain.restaurant.restaurant.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.restaurant.restaurant.dto.CategoryFindListResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.CategoryFindResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantFindListResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantFindResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantSummaryResponse;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantCategory;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantQueryService {

	private final RestaurantRepository restaurantRepository;

	@Transactional(readOnly = true)
	public RestaurantFindListResponse getRestaurants() {
		List<RestaurantSummaryResponse> summarizedRestaurants = restaurantRepository.findAllSummary();

		return new RestaurantFindListResponse(summarizedRestaurants);
	}

	@Transactional(readOnly = true)
	public RestaurantFindResponse getRestaurant(Long restaurantId) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new CustomException(RESTAURANT_NOT_FOUND));

		return RestaurantFindResponse.from(restaurant);
	}

	@Transactional(readOnly = true)
	public CategoryFindResponse getCategoryFromRestaurant(Long restaurantId) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new CustomException(RESTAURANT_NOT_FOUND));

		RestaurantCategory category = restaurant.getRestaurantCategory();

		return new CategoryFindResponse(category.name());
	}

	@Transactional(readOnly = true)
	public CategoryFindListResponse getAllCategories() {
		List<String> categories = RestaurantCategory.getAllCategories();

		return new CategoryFindListResponse(categories);
	}
}
