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
	public void updateReview(long memberId, Long reviewId, ReviewUpdateRequest request) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));

		validateReviewOwnership(review, memberId);

		int oldRate = review.getRate();

		review.update(request.title(), request.body(), request.rate());

		Restaurant restaurant = review.getRestaurant();
		restaurant.updateReviewAndUpdateAverageRate(oldRate, review.getRate());
	}

	@Transactional
	public void deleteReview(Long reviewId, long memberId) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new CustomException(REVIEW_NOT_FOUND));

		validateReviewOwnership(review, memberId);

		Restaurant restaurant = review.getRestaurant();
		int oldRate = review.getRate();

		reviewRepository.delete(review);
		restaurant.removeReviewAndUpdateAverageRate(review);
	}

	private void validateReviewOwnership(Review review, long memberId) {
		if (!review.getMember().getId().equals(memberId)) {
			throw new CustomException(REVIEW_FORBIDDEN);
		}
	}
}
