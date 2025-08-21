package com.ll.commars.domain.favorite.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ll.commars.domain.favorite.entity.Favorite;
import com.ll.commars.domain.favorite.entity.FavoriteRestaurant;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;

public interface FavoriteRestaurantRepository extends JpaRepository<FavoriteRestaurant, Long> {

	boolean existsByFavoriteAndRestaurant(Favorite favorite, Restaurant restaurant);

	@Modifying
	@Query("DELETE FROM FavoriteRestaurant fr WHERE fr.restaurant.id = :restaurantId AND fr.favorite.id IN (SELECT f.id FROM Favorite f WHERE f.member.id = :memberId)")
	void deleteAllByRestaurantIdAndMemberId(@Param("restaurantId") Long restaurantId, @Param("memberId") Long memberId);
}
