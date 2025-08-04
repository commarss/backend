package com.ll.commars.domain.restaurant.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

	@Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.restaurantMenus")
	List<Restaurant> findAllWithMenus();

	Optional<Restaurant> findByName(String name);

	List<Restaurant> findByRestaurantCategoryId(Long categoryId);

	@Query(value = """
		    SELECT r FROM Restaurant r
		    WHERE (6371 * ACOS(
		        COS(RADIANS(:lat)) * COS(RADIANS(r.lat)) 
		        * COS(RADIANS(r.lon) - RADIANS(:lon)) 
		        + SIN(RADIANS(:lat)) * SIN(RADIANS(r.lat))
		    )) <= 2
		""")
	List<Restaurant> findRestaurantsWithinRadius(@Param("lat") double lat, @Param("lon") double lon);
}
