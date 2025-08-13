package com.ll.commars.domain.restaurant.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.restaurant.entity.RestaurantMenu;

public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, Long> {

}
