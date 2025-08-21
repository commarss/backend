package com.ll.commars.domain.favorite.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.favorite.entity.Favorite;
import com.ll.commars.domain.favorite.entity.FavoriteRestaurant;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;

public interface FavoriteRestaurantRepository extends JpaRepository<FavoriteRestaurant, Long> {

	boolean existsByFavoriteAndRestaurant(Favorite favorite, Restaurant restaurant);
}
