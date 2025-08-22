package com.ll.commars.domain.restaurant.restaurant.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantCreateRequest;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantCreateResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantUpdateRequest;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantCategory;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.domain.review.dto.ReviewCreateRequest;
import com.ll.commars.domain.review.dto.ReviewCreateResponse;
import com.ll.commars.domain.review.entity.Review;
import com.ll.commars.domain.review.repository.jpa.ReviewRepository;
import com.ll.commars.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantCommandService {

	private final RestaurantRepository restaurantRepository;
	private final MemberRepository memberRepository;
	private final ReviewRepository reviewRepository;

	@Transactional
	public RestaurantCreateResponse createRestaurant(RestaurantCreateRequest request) {
		RestaurantCategory restaurantCategory = RestaurantCategory.fromString(request.category());

		Restaurant restaurant = new Restaurant(request.name(), request.details(),
			request.imageUrl(), request.contact(), request.address(), restaurantCategory);

		// todo: 추후 주소를 토대로 lat, lng을 계산하는 API 호출 로직 구현 - 스프링 이벤트 사용

		return RestaurantCreateResponse.from(restaurantRepository.save(restaurant));
	}

	@Transactional
	public void updateRestaurant(Long restaurantId,
		RestaurantUpdateRequest request) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new CustomException(RESTAURANT_NOT_FOUND));

		restaurant.updateRestaurant(request.name(), request.details(), request.imageUrl(),
			request.contact(), request.address(), request.category());
	}

	@Transactional
	public void deleteRestaurant(Long restaurantId) {
		restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new CustomException(RESTAURANT_NOT_FOUND));

		restaurantRepository.deleteById(restaurantId);
	}

	@Transactional
	public ReviewCreateResponse createReview(Long restaurantId, ReviewCreateRequest request) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
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

		restaurant.addReviewAndUpdateAverageRate(review);
		review.addRestaurant(restaurant);

		return ReviewCreateResponse.from(reviewRepository.save(review));
	}
}
