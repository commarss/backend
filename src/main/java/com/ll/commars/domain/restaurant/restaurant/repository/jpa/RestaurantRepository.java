package com.ll.commars.domain.restaurant.restaurant.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, RestaurantRepositoryCustom {

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
