package com.ll.commars.domain.restaurant.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ll.commars.domain.restaurant.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.entity.RestaurantMenu;

public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, Long> {

	//List<RestaurantMenuDto.MenuInfo> findByRestaurantId(Long id);
	@Query("SELECT m FROM RestaurantMenu m WHERE m.restaurant.id = :restaurantId")
	List<RestaurantMenuDto.MenuInfo> findByRestaurantId(@Param("restaurantId") Long id);
}
