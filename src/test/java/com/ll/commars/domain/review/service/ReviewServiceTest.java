package com.ll.commars.domain.review.service;

import static com.ll.commars.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.fixture.MemberFixture;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.fixture.RestaurantFixture;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.domain.review.dto.ReviewUpdateRequest;
import com.ll.commars.domain.review.entity.Review;
import com.ll.commars.domain.review.fixture.ReviewFixture;
import com.ll.commars.domain.review.repository.jpa.ReviewRepository;
import com.ll.commars.global.annotation.IntegrationTest;
import com.ll.commars.global.exception.CustomException;

import com.navercorp.fixturemonkey.FixtureMonkey;

@IntegrationTest
@DisplayName("ReviewService 테스트")
class ReviewServiceTest {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private FixtureMonkey fixtureMonkey;

	private Member member1;
	private Member member2;
	private Restaurant restaurant;
	private Review review;

	private static final long INVALID_REVIEW_ID = 99999L;

	@BeforeEach
	void setUp() {
		MemberFixture memberFixture = new MemberFixture(fixtureMonkey, memberRepository);
		RestaurantFixture restaurantFixture = new RestaurantFixture(fixtureMonkey, restaurantRepository);
		ReviewFixture reviewFixture = new ReviewFixture(fixtureMonkey, reviewRepository);

		String encodedPassword = passwordEncoder.encode("password");
		member1 = memberFixture.이메일_사용자("user1@test.com", encodedPassword);
		member2 = memberFixture.이메일_사용자("user2@test.com", encodedPassword);
		restaurant = restaurantFixture.한식_식당();

		review = reviewFixture.리뷰(member1, restaurant, "원본 제목", "원본 내용", 4);
		restaurant.addReviewAndUpdateAverageRate(review);
		restaurantRepository.save(restaurant);
	}

	@Nested
	class 리뷰_수정_테스트 {

		@Test
		void 성공적으로_리뷰를_수정한다() {
			// given
			final int oldRate = review.getRate();
			final long initialTotalRateSum = restaurant.getTotalRateSum();
			final ReviewUpdateRequest request = new ReviewUpdateRequest("수정된 제목", "수정된 내용", 5);

			// when
			reviewService.updateReview(member1.getId(), review.getId(), request);

			// then
			Review updatedReview = reviewRepository.findById(review.getId()).get();
			Restaurant updatedRestaurant = restaurantRepository.findById(restaurant.getId()).get();

			long expectedTotalRateSum = initialTotalRateSum - oldRate + request.rate();
			double expectedAverageRate = Math.round(
				((double)expectedTotalRateSum / updatedRestaurant.getReviewCount()) * 10) / 10.0;

			assertAll(
				() -> assertThat(updatedReview.getTitle()).isEqualTo(request.title()),
				() -> assertThat(updatedReview.getBody()).isEqualTo(request.body()),
				() -> assertThat(updatedReview.getRate()).isEqualTo(request.rate()),
				() -> assertThat(updatedRestaurant.getTotalRateSum()).isEqualTo(expectedTotalRateSum),
				() -> assertThat(updatedRestaurant.getAverageRate()).isEqualTo(expectedAverageRate)
			);
		}

		@Test
		void 존재하지_않는_리뷰를_수정하면_예외가_발생한다() {
			// given
			ReviewUpdateRequest request = new ReviewUpdateRequest("제목", "내용", 3);

			// when & then
			assertThatThrownBy(() -> reviewService.updateReview(member1.getId(), INVALID_REVIEW_ID, request))
				.isInstanceOf(CustomException.class)
				.hasMessage(REVIEW_NOT_FOUND.getMessage());
		}

		@Test
		void 리뷰_작성자가_아닌_사용자가_수정을_시도하면_예외가_발생한다() {
			// given
			ReviewUpdateRequest request = new ReviewUpdateRequest("제목", "내용", 3);

			// when & then
			assertThatThrownBy(() -> reviewService.updateReview(member2.getId(), review.getId(), request))
				.isInstanceOf(CustomException.class)
				.hasMessage(REVIEW_FORBIDDEN.getMessage());
		}
	}

	@Nested
	class 리뷰_삭제_테스트 {

		@Test
		void 성공적으로_리뷰를_삭제한다() {
			// given
			int initialReviewCount = restaurant.getReviewCount();
			long initialTotalRateSum = restaurant.getTotalRateSum();
			int deletedRate = review.getRate();

			// when
			reviewService.deleteReview(review.getId(), member1.getId());

			// then
			Restaurant updatedRestaurant = restaurantRepository.findById(restaurant.getId()).get();

			int expectedReviewCount = initialReviewCount - 1;
			long expectedTotalRateSum = initialTotalRateSum - deletedRate;
			double expectedAverageRate = (expectedReviewCount > 0) ?
				Math.round(((double)expectedTotalRateSum / expectedReviewCount) * 10) / 10.0 : 0.0;

			assertAll(
				() -> assertThat(reviewRepository.findById(review.getId())).isEmpty(),
				() -> assertThat(updatedRestaurant.getReviewCount()).isEqualTo(expectedReviewCount),
				() -> assertThat(updatedRestaurant.getTotalRateSum()).isEqualTo(expectedTotalRateSum),
				() -> assertThat(updatedRestaurant.getAverageRate()).isEqualTo(expectedAverageRate)
			);
		}

		@Test
		void 존재하지_않는_리뷰를_삭제하면_예외가_발생한다() {
			// when & then
			assertThatThrownBy(() -> reviewService.deleteReview(INVALID_REVIEW_ID, member1.getId()))
				.isInstanceOf(CustomException.class)
				.hasMessage(REVIEW_NOT_FOUND.getMessage());
		}

		@Test
		void 리뷰_작성자가_아닌_사용자가_삭제를_시도하면_예외가_발생한다() {
			// when & then
			assertThatThrownBy(() -> reviewService.deleteReview(review.getId(), member2.getId()))
				.isInstanceOf(CustomException.class)
				.hasMessage(REVIEW_FORBIDDEN.getMessage());
		}
	}
}
