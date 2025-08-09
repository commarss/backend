package com.ll.commars.domain.review.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.domain.restaurant.restaurant.service.RestaurantService;
import com.ll.commars.domain.review.entity.Review;
import com.ll.commars.domain.review.dto.ReviewDto;
import com.ll.commars.domain.review.repository.jpa.ReviewRepository;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final RestaurantRepository restaurantRepository;
	private final MemberRepository memberRepository;
	private final RestaurantService restaurantService;
	private final MemberService memberService;

	public void truncate() {
		reviewRepository.deleteAll();
	}

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

	public Review wirteReview(String restaurantId, String username, String name, String body, int rate) {
		Restaurant restaurant = restaurantService.findById(Long.valueOf(restaurantId));
		Optional<Member> user = memberService.findById(Long.parseLong(username));

		Review review = Review.builder()
			.name(name)
			.body(body)
			.rate(rate)
			.restaurant(restaurant)
			.member(user.orElseThrow(() -> new IllegalArgumentException("User not found")))
			.build();

		reviewRepository.save(review);

		Long restaurantId2Long = Long.valueOf(restaurantId);

		// 해당 식당의 모든 리뷰 평점 평균 계산
		List<Review> allReviews = reviewRepository.findByRestaurantId(restaurantId2Long);
		double newAverageRate = allReviews.stream()
			.mapToInt(Review::getRate)
			.average()
			.orElse(0.0);
		System.out.println("newAverageRate = " + newAverageRate);

		// 식당의 평균 평점 업데이트
		restaurant.setAverageRate(newAverageRate);
		restaurantRepository.save(restaurant);

		return review;
	}
}
