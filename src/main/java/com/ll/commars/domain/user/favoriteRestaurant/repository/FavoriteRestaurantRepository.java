package com.ll.commars.domain.user.favoriteRestaurant.repository;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.user.favoriteRestaurant.entity.FavoriteRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface FavoriteRestaurantRepository extends JpaRepository<FavoriteRestaurant, Long> {
    List<FavoriteRestaurant> findByFavoriteId(Long favoriteId);
    void deleteByFavoriteIdAndRestaurantId(Long favoriteId, Long restaurantId);
    void deleteByFavoriteId(Long favoriteId);

    // 새로운 메서드: favoriteId 목록으로 FavoriteRestaurant 조회
    List<FavoriteRestaurant> findByFavoriteIdIn(Set<Long> favoriteIds);  // Set<Long>을 받을 수 있도록 수정
}
