package com.ll.commars.domain.favorite.favorite.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ll.commars.domain.favorite.favorite.entity.Favorite;
import com.ll.commars.domain.member.entity.Member;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	// 특정 리뷰어(email)의 찜 목록을 조회
	List<Favorite> findByMemberEmail(String email);

	// 특정 리뷰어와 식당에 대한 찜이 이미 존재하는지 체크
	Optional<Favorite> findByMemberEmailAndId(String email, Long restaurantId);

	@Query("SELECT COUNT(f) FROM Favorite f WHERE f.member.email = :email")
	int countByUserEmail(@Param("email") String email);

	// 유저 ID로 찜 목록 조회
	List<Favorite> findByMemberId(Long memberId);

	boolean existsByMemberAndFavoriteRestaurantsRestaurantId(Member member, Long restaurantId);

	Optional<Favorite> findByMemberAndName(Member member, String name);

	Optional<Favorite> findByMemberAndFavoriteRestaurantsRestaurantId(Member member, Long restaurantId);
}
