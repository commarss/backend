package com.ll.commars.domain.favorite.favorite.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ll.commars.domain.favorite.favorite.entity.Favorite;
import com.ll.commars.domain.user.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	// 특정 리뷰어(email)의 찜 목록을 조회
	List<Favorite> findByUserEmail(String email);

	// 특정 리뷰어와 식당에 대한 찜이 이미 존재하는지 체크
	Optional<Favorite> findByUserEmailAndId(String email, Long restaurantId);

	@Query("SELECT COUNT(f) FROM Favorite f WHERE f.user.email = :email")
	int countByUserEmail(@Param("email") String email);

	// 유저 ID로 찜 목록 조회
	List<Favorite> findByUserId(Long userId);

	boolean existsByUserAndFavoriteRestaurantsRestaurantId(User user, Long restaurantId);

	Optional<Favorite> findByUserAndName(User user, String name);

	Optional<Favorite> findByUserAndFavoriteRestaurantsRestaurantId(User user, Long restaurantId);
}
