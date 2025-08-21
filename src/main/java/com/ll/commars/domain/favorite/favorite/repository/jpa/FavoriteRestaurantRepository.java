package com.ll.commars.domain.favorite.favorite.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.favorite.favorite.entity.FavoriteRestaurant;

public interface FavoriteRestaurantRepository extends JpaRepository<FavoriteRestaurant, Long> {

}
