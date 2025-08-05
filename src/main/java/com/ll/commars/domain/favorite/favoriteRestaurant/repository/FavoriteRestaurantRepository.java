package com.ll.commars.domain.favorite.favoriteRestaurant.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.favorite.favoriteRestaurant.entity.FavoriteRestaurant;

public interface FavoriteRestaurantRepository extends JpaRepository<FavoriteRestaurant, Long> {

	List<FavoriteRestaurant> findByFavoriteId(Long favoriteId);

	void deleteByFavoriteIdAndRestaurantId(Long favoriteId, Long restaurantId);

	void deleteByFavoriteId(Long favoriteId);

	// 새로운 메서드: favoriteId 목록으로 FavoriteRestaurant 조회
	List<FavoriteRestaurant> findByFavoriteIdIn(Set<Long> favoriteIds);  // Set<Long>을 받을 수 있도록 수정
}
