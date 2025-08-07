package com.ll.commars.domain.reviewerRank.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.RestaurantRepository;
import com.ll.commars.domain.review.review.dto.ReviewDto;
import com.ll.commars.domain.review.review.entity.Review;
import com.ll.commars.domain.review.review.repository.ReviewRepository;
import com.ll.commars.domain.reviewerRank.dto.ReviewerRank;
import com.ll.commars.domain.reviewerRank.dto.ReviewerRankResponse;
import com.ll.commars.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewerService {

	public final MemberRepository memberRepository;
	public final RestaurantRepository restaurantRepository;
	private final ReviewRepository reviewRepository;

	// 상위 10명의 리뷰어 조회
	// ✅ 리뷰 작성 메서드 추가
	@Transactional
	public void writeReview(Long restaurantId, ReviewDto.ReviewWriteRequest request, String userEmail) {
		Member member = memberRepository.findByEmail(userEmail)
			.orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("레스토랑을 찾을 수 없습니다."));

		Review review = Review.builder()
			.name(request.getReviewName())
			.body(request.getBody())
			.rate(request.getRate())
			.member(member)
			.restaurant(restaurant)
			.build();

		reviewRepository.saveAndFlush(review); // 🔥 즉시 저장!
	}

	// ✅ 상위 10명의 리뷰어 조회 후 top3, others 분리
	public ReviewerRankResponse getTopReviewers() {
		List<ReviewerRank> topReviewers = reviewRepository.findTopReviewers(PageRequest.of(0, 10));
		System.out.println("🔹 상위 리뷰어 수: " + topReviewers.size());

		// 상위 3명과 그 외 유저 분리
		List<ReviewerRank> top3Reviewers = topReviewers.subList(0, Math.min(3, topReviewers.size()));
		List<ReviewerRank> otherReviewers = topReviewers.subList(Math.min(3, topReviewers.size()), topReviewers.size());

		return new ReviewerRankResponse(top3Reviewers, otherReviewers);
	}

	@Transactional
	public void truncate() {

		reviewRepository.deleteAll();

	}
}
