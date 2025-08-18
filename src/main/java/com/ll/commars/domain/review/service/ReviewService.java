package com.ll.commars.domain.review.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.domain.review.dto.ReviewCreateRequest;
import com.ll.commars.domain.review.dto.ReviewCreateResponse;
import com.ll.commars.domain.review.dto.ReviewUpdateRequest;
import com.ll.commars.domain.review.entity.Review;
import com.ll.commars.domain.review.repository.jpa.ReviewRepository;
import com.ll.commars.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final RestaurantRepository restaurantRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public ReviewCreateResponse createReview(ReviewCreateRequest request) {
		Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
			.orElseThrow(() -> new CustomException(RESTAURANT_NOT_FOUND));

		Member member = memberRepository.findById(request.userId())
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		Review review = new Review(
			request.title(),
			request.body(),
			request.rate(),
			restaurant,
			member
		);

		restaurant.addReviewAndUpdateAverageRate(review.getRate());

		return ReviewCreateResponse.from(reviewRepository.save(review));
	}

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
			throw new CustomException(REIVIEW_NOT_UNAUTHORIZED);
		}
	}
}
