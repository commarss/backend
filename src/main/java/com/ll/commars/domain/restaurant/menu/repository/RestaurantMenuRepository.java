package com.ll.commars.domain.restaurant.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.restaurant.entity.Menu;

public interface RestaurantMenuRepository extends JpaRepository<Menu, Long> {

}
