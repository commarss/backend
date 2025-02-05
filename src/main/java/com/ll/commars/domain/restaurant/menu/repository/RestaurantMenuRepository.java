package com.ll.commars.domain.restaurant.menu.repository;

import com.ll.commars.domain.restaurant.menu.entity.RestaurantMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, Long> {
    List<RestaurantMenu> findByRestaurantId(Long id);
}
