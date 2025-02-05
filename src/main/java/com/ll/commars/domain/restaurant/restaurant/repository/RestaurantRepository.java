package com.ll.commars.domain.restaurant.restaurant.repository;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.restaurantMenus")
    List<Restaurant> findAllWithMenus();

    Optional<Restaurant> findByName(String name);
}
