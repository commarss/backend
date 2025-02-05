package com.ll.commars.domain.restaurant.category.repository;

import com.ll.commars.domain.restaurant.category.entity.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, Long> {
}
