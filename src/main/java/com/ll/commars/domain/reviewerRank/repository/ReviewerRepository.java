package com.ll.commars.domain.reviewerRank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.review.review.entity.Review;

public interface ReviewerRepository extends JpaRepository<Review, Long> {

	//    // ✅ 상위 10명의 리뷰어 조회 (LIMIT 제거 및 Pageable 추가)
	//    @Query("SELECT new com.ll.commars.domain.reviewerRank.dto.ReviewerRank(r.user.name, COUNT(r)) " +
	//            "FROM Review r " +
	//            "GROUP BY r.user.name " +
	//            "ORDER BY COUNT(r) DESC")
	//    List<ReviewerRank> findTopReviewers(Pageable pageable);
}
