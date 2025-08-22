package com.ll.commars.domain.restaurant.restaurant.service;

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
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantCreateRequest;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantCreateResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantUpdateRequest;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantCategory;
import com.ll.commars.domain.restaurant.restaurant.fixture.RestaurantFixture;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.domain.review.dto.ReviewCreateRequest;
import com.ll.commars.domain.review.dto.ReviewCreateResponse;
import com.ll.commars.domain.review.entity.Review;
import com.ll.commars.domain.review.repository.jpa.ReviewRepository;
import com.ll.commars.global.annotation.IntegrationTest;
import com.ll.commars.global.exception.CustomException;

import com.navercorp.fixturemonkey.FixtureMonkey;

@IntegrationTest
@DisplayName("RestaurantCommandService 테스트")
public class RestaurantCommandServiceTest {

	@Autowired
	private RestaurantCommandService restaurantCommandService;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private FixtureMonkey fixtureMonkey;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private Restaurant koreanRestaurant;

	private Restaurant chineseRestaurant;
	private Restaurant westernRestaurant;

	private static final long INVALID_RESTAURANT_ID = 99999L;

	@BeforeEach
	void setUp() {
		RestaurantFixture restaurantFixture = new RestaurantFixture(fixtureMonkey, restaurantRepository);

		koreanRestaurant = restaurantFixture.한식_식당();
		chineseRestaurant = restaurantFixture.중식_식당();
		westernRestaurant = restaurantFixture.양식_식당();
	}

	@Nested
	class 레스토랑_생성_테스트 {

		@Test
		void 성공적으로_레스토랑을_생성한다() {
			// given
			RestaurantCreateRequest request = new RestaurantCreateRequest(
				koreanRestaurant.getName(),
				koreanRestaurant.getDetails(),
				koreanRestaurant.getImageUrl(),
				koreanRestaurant.getContact(),
				koreanRestaurant.getAddress(),
				koreanRestaurant.getRestaurantCategory().name()
			);

			// when
			RestaurantCreateResponse response = restaurantCommandService.createRestaurant(request);

			// then
			assertAll(
				() -> assertThat(response).isNotNull(),
				() -> assertThat(response.id()).isNotNull(),
				() -> assertThat(restaurantRepository.findById(response.id())).isPresent()
			);
		}
	}

	@Nested
	class 레스토랑_수정_테스트 {

		@Test
		void 성공적으로_레스토랑_정보를_수정한다() {
			// given
			RestaurantUpdateRequest request = new RestaurantUpdateRequest(
				"수정된 식당",
				"수정된 식당 설명",
				null,
				null,
				"수정된 주소",
				"일식"
			);

			// when
			restaurantCommandService.updateRestaurant(westernRestaurant.getId(), request);

			// then
			Restaurant updatedRestaurant = restaurantRepository.findById(westernRestaurant.getId()).get();

			assertAll(
				() -> assertThat(updatedRestaurant.getName()).isEqualTo(request.name()),
				() -> assertThat(updatedRestaurant.getRestaurantCategory()).isEqualTo(RestaurantCategory.일식)
			);
		}

		@Test
		void 존재하지_않는_레스토랑을_수정하면_예외가_발생한다() {
			// given
			RestaurantUpdateRequest request = new RestaurantUpdateRequest(
				"수정된 식당",
				"수정된 식당 설명",
				null,
				null,
				"수정된 주소",
				"일식"
			);

			// when & then
			assertThatThrownBy(() -> restaurantCommandService.updateRestaurant(INVALID_RESTAURANT_ID, request))
				.isInstanceOf(CustomException.class)
				.hasMessage(RESTAURANT_NOT_FOUND.getMessage());
		}
	}

	@Nested
	class 레스토랑_삭제_테스트 {

		@Test
		void 성공적으로_레스토랑을_삭제한다() {
			// when
			restaurantCommandService.deleteRestaurant(chineseRestaurant.getId());

			// then
			assertThat(restaurantRepository.findById(chineseRestaurant.getId())).isEmpty();
		}

		@Test
		void 존재하지_않는_레스토랑을_삭제하면_예외가_발생한다() {
			// when & then
			assertThatThrownBy(() -> restaurantCommandService.deleteRestaurant(INVALID_RESTAURANT_ID))
				.isInstanceOf(CustomException.class)
				.hasMessage(RESTAURANT_NOT_FOUND.getMessage());
		}
	}

	@Nested
	class 리뷰_생성_테스트 {

		private Member member;
		private Restaurant restaurant;
		private static final long INVALID_MEMBER_ID = 99999L;

		@BeforeEach
		void setUp() {
			RestaurantFixture restaurantFixture = new RestaurantFixture(fixtureMonkey, restaurantRepository);
			MemberFixture memberFixture = new MemberFixture(fixtureMonkey, memberRepository);

			restaurant = restaurantFixture.양식_식당();

			String encodedPassword = passwordEncoder.encode("password");
			member = memberFixture.이메일_사용자("test@test.com", encodedPassword);
		}

		@Test
		void 성공적으로_리뷰를_생성한다() {
			// given
			final int rate = 5;
			ReviewCreateRequest request = new ReviewCreateRequest(
				member.getId(),
				"맛있어요",
				"정말 최고의 맛집입니다.",
				rate
			);
			long initialTotalRateSum = restaurant.getTotalRateSum();
			int initialReviewCount = restaurant.getReviewCount();

			// when
			ReviewCreateResponse response = restaurantCommandService.createReview(restaurant.getId(), request);

			// then
			Restaurant updatedRestaurant = restaurantRepository.findById(restaurant.getId()).get();
			Review createdReview = reviewRepository.findById(response.id()).get();
			double expectedAverageRate = Math.round(
				((double)(initialTotalRateSum + createdReview.getRate()) / (initialReviewCount + 1)) * 10) / 10.0;

			assertAll(
				() -> assertThat(response).isNotNull(),
				() -> assertThat(response.title()).isEqualTo(request.title()),
				() -> assertThat(response.body()).isEqualTo(request.body()),
				() -> assertThat(response.rate()).isEqualTo(request.rate()),
				() -> assertThat(updatedRestaurant.getReviewCount()).isEqualTo(initialReviewCount + 1),
				() -> assertThat(updatedRestaurant.getTotalRateSum()).isEqualTo(initialTotalRateSum + rate),
				() -> assertThat(updatedRestaurant.getAverageRate()).isEqualTo(expectedAverageRate)
			);
		}

		@Test
		void 존재하지_않는_식당에_리뷰를_작성하면_예외가_발생한다() {
			// given
			ReviewCreateRequest request = new ReviewCreateRequest(
				member.getId(),
				"제목",
				"내용",
				4
			);

			// when & then
			assertThatThrownBy(() -> restaurantCommandService.createReview(INVALID_RESTAURANT_ID, request))
				.isInstanceOf(CustomException.class)
				.hasMessage(RESTAURANT_NOT_FOUND.getMessage());
		}

		@Test
		void 존재하지_않는_사용자가_리뷰를_작성하면_예외가_발생한다() {
			// given
			ReviewCreateRequest request = new ReviewCreateRequest(
				INVALID_MEMBER_ID,
				"제목",
				"내용",
				3
			);

			// when & then
			assertThatThrownBy(() -> restaurantCommandService.createReview(restaurant.getId(), request))
				.isInstanceOf(CustomException.class)
				.hasMessage(MEMBER_NOT_FOUND.getMessage());
		}
	}
}
