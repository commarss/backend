package com.ll.commars.domain.user.favoriteRestaurant.repository;

import com.ll.commars.domain.user.favoriteRestaurant.entity.FavoriteRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRestaurantRepository extends JpaRepository<FavoriteRestaurant, Long> {
    List<FavoriteRestaurant> findByFavoriteId(Long favoriteId);
    void deleteByFavoriteIdAndRestaurantId(Long favoriteId, Long restaurantId);
    void deleteByFavoriteId(Long favoriteId);
}
