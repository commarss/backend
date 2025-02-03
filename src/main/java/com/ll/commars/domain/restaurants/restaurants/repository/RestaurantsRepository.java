package com.ll.commars.domain.restaurants.restaurants.repository;

import com.ll.commars.domain.restaurants.restaurants.entity.Restaurants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantsRepository extends JpaRepository<Restaurants, Long> {
}
