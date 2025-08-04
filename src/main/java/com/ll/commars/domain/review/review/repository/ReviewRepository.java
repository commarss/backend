package com.ll.commars.domain.review.review.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ll.commars.domain.review.review.entity.Review;
import com.ll.commars.domain.reviewerRank.dto.ReviewerRank;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

	//    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.restaurant = :restaurant")
	List<Review> findByRestaurantId(Long restaurantId);

	// ✅ 상위 10명의 리뷰어 조회 (LIMIT 제거 및 Pageable 추가)
	@Query("SELECT new com.ll.commars.domain.reviewerRank.dto.ReviewerRank(r.user.id, r.user.name, COUNT(r)) " +
		"FROM Review r " +
		"GROUP BY r.user.id, r.user.name " +
		"ORDER BY COUNT(r) DESC")
	List<ReviewerRank> findTopReviewers(Pageable pageable);

}
