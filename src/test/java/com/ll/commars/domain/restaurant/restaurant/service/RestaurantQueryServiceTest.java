package com.ll.commars.domain.restaurant.restaurant.service;

import static com.ll.commars.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ll.commars.domain.restaurant.restaurant.dto.CategoryFindListResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.CategoryFindResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantFindListResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantFindResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantSummaryResponse;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantCategory;
import com.ll.commars.domain.restaurant.restaurant.fixture.RestaurantFixture;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.global.annotation.IntegrationTest;
import com.ll.commars.global.exception.CustomException;

import com.navercorp.fixturemonkey.FixtureMonkey;

@IntegrationTest
@DisplayName("RestaurantQueryService 테스트")
public class RestaurantQueryServiceTest {

	@Autowired
	private RestaurantQueryService restaurantQueryService;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private RestaurantFixture restaurantFixture;

	@Autowired
	private FixtureMonkey fixtureMonkey;

	private static final long INVALID_RESTAURANT_ID = 99999L;

	private Restaurant koreanRestaurant;
	private Restaurant chineseRestaurant;
	private Restaurant westernRestaurant;

	@BeforeEach
	void setUp() {
		restaurantFixture = new RestaurantFixture(fixtureMonkey, restaurantRepository);

		koreanRestaurant = restaurantFixture.한식_식당();
		chineseRestaurant = restaurantFixture.중식_식당();
		westernRestaurant = restaurantFixture.양식_식당();
	}

	@Nested
	class 전체_식당_조회_테스트 {

		@Test
		void 저장된_모든_식당을_성공적으로_조회한다() {
			// when
			RestaurantFindListResponse response = restaurantQueryService.getRestaurants();

			List<Long> restaurantIds = response.restaurants().stream()
				.map(RestaurantSummaryResponse::id)
				.toList();

			// then
			assertAll(
				() -> assertThat(response).isNotNull(),
				() -> assertThat(response.restaurants()).isNotNull(),
				() -> assertThat(restaurantIds).contains(koreanRestaurant.getId(), chineseRestaurant.getId(), westernRestaurant.getId())
			);
		}

		@Test
		void 식당이_없을_때_빈_목록을_반환한다() {
			// given
			restaurantRepository.deleteAll();

			// when
			RestaurantFindListResponse response = restaurantQueryService.getRestaurants();

			// then
			assertAll(
				() -> assertThat(response).isNotNull(),
				() -> assertThat(response.restaurants()).isEmpty()
			);
		}
	}

	@Nested
	class 단건_식당_조회_테스트 {

		@Test
		void 유효한_ID로_식당을_성공적으로_조회한다() {
			// when
			RestaurantFindResponse response = restaurantQueryService.getRestaurant(koreanRestaurant.getId());

			// then
			assertAll(
				() -> assertThat(response).isNotNull(),
				() -> assertThat(response.id()).isEqualTo(koreanRestaurant.getId()),
				() -> assertThat(response.name()).isEqualTo(koreanRestaurant.getName()),
				() -> assertThat(response.details()).isEqualTo(koreanRestaurant.getDetails()),
				() -> assertThat(response.address()).isEqualTo(koreanRestaurant.getAddress()),
				() -> assertThat(response.categoryName()).isEqualTo(koreanRestaurant.getRestaurantCategory().name())
			);
		}

		@Test
		void 존재하지_않는_ID로_조회시_예외가_발생한다() {
			// when & then
			assertThatThrownBy(() -> restaurantQueryService.getRestaurant(INVALID_RESTAURANT_ID))
				.isInstanceOf(CustomException.class)
				.hasMessage(RESTAURANT_NOT_FOUND.getMessage());
		}
	}

	@Nested
	class 카테고리별_식당_조회_테스트 {

		@Test
		void 특정_카테고리의_식당만_성공적으로_조회한다() {
			// when
			RestaurantFindListResponse response = restaurantQueryService.getRestaurantsByCategory("한식");

			// then
			assertAll(
				() -> assertThat(response).isNotNull(),
				() -> assertThat(response.restaurants()).isNotEmpty(),
				() -> assertThat(response.restaurants()).allSatisfy(
					restaurant -> assertThat(restaurant.categoryName()).isEqualTo("한식"))
			);
		}

		@Test
		void 존재하지_않는_카테고리로_조회시_예외가_발생한다() {
			// when & then
			assertThatThrownBy(() -> restaurantQueryService.getRestaurantsByCategory("존재하지않는카테고리"))
				.isInstanceOf(CustomException.class)
				.hasMessageContaining(CATEGORY_NOT_FOUND.getMessage());
		}

		@Test
		void 해당_카테고리의_식당이_없을_때_빈_목록을_반환한다() {
			// when
			RestaurantFindListResponse response = restaurantQueryService.getRestaurantsByCategory("일식");

			// then
			assertAll(
				() -> assertThat(response).isNotNull(),
				() -> assertThat(response.restaurants()).isEmpty()
			);
		}
	}

	@Nested
	class 식당_카테고리_조회_테스트 {

		@Test
		void 유효한_식당_ID로_카테고리를_성공적으로_조회한다() {
			// when
			CategoryFindResponse response = restaurantQueryService.getCategoryFromRestaurant(koreanRestaurant.getId());

			// then
			assertAll(
				() -> assertThat(response).isNotNull(),
				() -> assertThat(response.category()).isEqualTo(koreanRestaurant.getRestaurantCategory().name())
			);
		}

		@Test
		void 존재하지_않는_식당_ID로_카테고리_조회시_예외가_발생한다() {
			// when & then
			assertThatThrownBy(() -> restaurantQueryService.getCategoryFromRestaurant(INVALID_RESTAURANT_ID))
				.isInstanceOf(CustomException.class)
				.hasMessage(RESTAURANT_NOT_FOUND.getMessage());
		}
	}

	@Nested
	class 전체_카테고리_조회_테스트 {

		@Test
		void 모든_카테고리를_성공적으로_조회한다() {
			// when
			CategoryFindListResponse response = restaurantQueryService.getAllCategories();

			List<String> expectedCategories = Arrays.stream(RestaurantCategory.values())
				.map(Enum::name)
				.toList();

			// then
			assertThat(response.categories()).containsExactlyInAnyOrderElementsOf(expectedCategories);
		}

		@Test
		void 카테고리_목록이_예상된_개수와_일치한다() {
			// when
			CategoryFindListResponse response = restaurantQueryService.getAllCategories();

			// then
			assertThat(response.categories()).hasSize(RestaurantCategory.values().length);
		}
	}
}
