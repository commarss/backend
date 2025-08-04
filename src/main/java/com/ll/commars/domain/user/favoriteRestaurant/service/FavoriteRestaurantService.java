package com.ll.commars.domain.user.favoriteRestaurant.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.user.favoriteRestaurant.repository.FavoriteRestaurantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteRestaurantService {

	private final FavoriteRestaurantRepository favoriteRestaurantRepository;

	public void truncate() {
		favoriteRestaurantRepository.deleteAll();
	}

	@Transactional
	public void deleteRestaurantFromFavorite(long favoriteId, long restaurantId) {
		favoriteRestaurantRepository.deleteByFavoriteIdAndRestaurantId(favoriteId, restaurantId);
	}
}
