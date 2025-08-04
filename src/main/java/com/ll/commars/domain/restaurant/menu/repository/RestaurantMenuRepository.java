package com.ll.commars.domain.restaurant.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ll.commars.domain.restaurant.menu.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.menu.entity.RestaurantMenu;

@Repository
public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, Long> {

	//List<RestaurantMenuDto.MenuInfo> findByRestaurantId(Long id);
	@Query("SELECT m FROM RestaurantMenu m WHERE m.restaurant.id = :restaurantId")
	List<RestaurantMenuDto.MenuInfo> findByRestaurantId(@Param("restaurantId") Long id);
}
