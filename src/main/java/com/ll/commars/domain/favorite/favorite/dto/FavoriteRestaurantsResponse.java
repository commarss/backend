package com.ll.commars.domain.favorite.favorite.dto;

import java.util.List;

import com.ll.commars.domain.favorite.favorite.entity.FavoriteRestaurant;

public record FavoriteRestaurantsResponse(
	List<FavoriteRestaurantInfo> restaurants,
	int totalCount
) {
	public static FavoriteRestaurantsResponse from(List<FavoriteRestaurant> favoriteRestaurants) {
		List<FavoriteRestaurantInfo> restaurants = favoriteRestaurants.stream()
			.map(fr -> new FavoriteRestaurantInfo(
				fr.getRestaurant().getId(),
				fr.getRestaurant().getName(),
				fr.getRestaurant().getAddress(),
				fr.getRestaurant().getRestaurantCategory().name(),
				fr.getRestaurant().getAverageRate(),
				fr.getRestaurant().getImageUrl()
			))
			.toList();

		return new FavoriteRestaurantsResponse(restaurants, restaurants.size());
	}

	public record FavoriteRestaurantInfo(
		Long id,
		String name,
		String address,
		String category,
		double averageRate,
		String imageUrl
	) {}
}
