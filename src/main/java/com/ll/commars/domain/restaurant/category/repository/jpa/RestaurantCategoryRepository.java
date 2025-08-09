package com.ll.commars.domain.restaurant.category.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.restaurant.category.entity.RestaurantCategory;

public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, Long> {

}
