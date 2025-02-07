package com.ll.commars.domain.restaurant.restaurant.repository;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
            * COS(RADIANS(r.lng) - RADIANS(:lng)) 
            + SIN(RADIANS(:lat)) * SIN(RADIANS(r.lat))
        )) <= 2
    """)
    List<Restaurant> findRestaurantsWithinRadius(@Param("lat") double lat, @Param("lng") double lng);
}
