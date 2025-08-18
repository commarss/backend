package com.ll.commars.domain.review.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.domain.member.service.MemberService;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.domain.restaurant.restaurant.service.RestaurantCommandService;
import com.ll.commars.domain.review.dto.ReviewDto;
import com.ll.commars.domain.review.entity.Review;
import com.ll.commars.domain.review.repository.jpa.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;

	@Transactional
	public void deleteReview(Long reviewId) {
		reviewRepository.findById(reviewId)
			.orElseThrow(() -> new IllegalArgumentException("Review not found"));

		reviewRepository.deleteById(reviewId);
	}

	@Transactional
	public ReviewDto.ReviewWriteResponse modifyReview(Long reviewId, ReviewDto.ReviewWriteRequest request) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new IllegalArgumentException("Review not found"));

		// 매개변수의 reviewId와 request의 userId가 일치하지 않으면 예외를 발생시킨다.
		if (!review.getMember().getId().equals(request.getUserId())) {
			throw new IllegalArgumentException("User not matched");
		}

		review.setName(request.getReviewName());
		review.setBody(request.getBody());
		review.setRate(request.getRate());

		return ReviewDto.ReviewWriteResponse.builder()
			.userName(review.getMember().getName())
			.restaurantName(review.getRestaurant().getName())
			.reviewName(review.getName())
			.body(review.getBody())
			.rate(review.getRate())
			.build();
	}

	public ReviewDto.ShowAllReviewsResponse showAllReviews(Long restaurantId) {
		List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);

		return ReviewDto.ShowAllReviewsResponse.builder()
			.reviews(reviews.stream()
				.map(review -> ReviewDto.ReviewInfo.builder()
					.id(review.getId())
					.userId(review.getMember().getId())
					.restaurantId(review.getRestaurant().getId())
					.name(review.getName())
					.body(review.getBody())
					.rate(review.getRate())
					.build())
				.toList())
			.build();
	}
}
