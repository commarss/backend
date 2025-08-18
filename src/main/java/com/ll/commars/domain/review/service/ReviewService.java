package com.ll.commars.domain.review.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.review.dto.ReviewUpdateRequest;
import com.ll.commars.domain.review.entity.Review;
import com.ll.commars.domain.review.repository.jpa.ReviewRepository;
import com.ll.commars.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;

	@Transactional
	public void updateReview(Long reviewId, ReviewUpdateRequest request) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));

		validateReviewOwnership(review, request.userId());

		int oldRate = review.getRate();

		review.update(request.title(), request.body(), request.rate());

		Restaurant restaurant = review.getRestaurant();
		restaurant.updateReviewAndUpdateAverageRate(oldRate, review.getRate());
	}

	private void validateReviewOwnership(Review review, Long userId) {
		if (!review.getMember().getId().equals(userId)) {
			throw new CustomException(REVIEW_NOT_UNAUTHORIZED);
		}
	}

	@Transactional
	public void deleteReview(Long reviewId) {
		// todo: 리뷰 삭제 권한 검증 필요
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));

		Restaurant restaurant = review.getRestaurant();
		int oldRate = review.getRate();

		reviewRepository.delete(review);
		restaurant.removeReviewAndUpdateAverageRate(oldRate);
	}
}
