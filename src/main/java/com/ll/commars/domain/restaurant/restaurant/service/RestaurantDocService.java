package com.ll.commars.domain.restaurant.restaurant.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantSearchResponse;
import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantDoc;
import com.ll.commars.domain.restaurant.restaurant.repository.elasticsearch.RestaurantDocRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantDocService {

	private final RestaurantDocRepository restaurantDocRepository;

	@Transactional(readOnly = true)
	public RestaurantSearchResponse searchRestaurants(String keyword, Double lat, Double lon, Double distance) {
		List<RestaurantDoc> restaurantDocs = restaurantDocRepository.searchByKeywordAndLocation(
			keyword,
			lat,
			lon,
			distance
		);

		return RestaurantSearchResponse.from(restaurantDocs);
	}

	@Transactional(readOnly = true)
	public RestaurantSearchResponse sortByRate() {
		List<RestaurantDoc> restaurantDocs = restaurantDocRepository.findTop5ByOrderByAverageRateDesc();

		return RestaurantSearchResponse.from(restaurantDocs);
	}

	@Transactional(readOnly = true)
	public RestaurantSearchResponse getNearbyRestaurants(Double lat, Double lon, Double distance) {
		List<RestaurantDoc> nearbyRestaurants = restaurantDocRepository
			.findNearbyRestaurantsSortedByDistance(lat, lon, distance);

		return RestaurantSearchResponse.from(nearbyRestaurants);
	}
}
