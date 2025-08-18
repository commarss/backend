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
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.global.annotation.IntegrationTest;
import com.ll.commars.global.exception.CustomException;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

@IntegrationTest
@DisplayName("RestaurantQueryService 테스트")
public class RestaurantQueryServiceTest {

	@Autowired
	private RestaurantQueryService restaurantQueryService;

	@Autowired
	private RestaurantRepository restaurantRepository;

	private static final String TEST_KOREAN_RESTAURANT_NAME = "테스트 한식당";
	private static final String TEST_CHINESE_RESTAURANT_NAME = "테스트 중식당";
	private static final String TEST_WESTERN_RESTAURANT_NAME = "테스트 양식당";
	private static final String DEFAULT_TEST_ADDRESS = "서울시 강남구";
	private static final long INVALID_RESTAURANT_ID = 99999L;

	private Restaurant 한식_식당;
	private Restaurant 중식_식당;
	private Restaurant 양식_식당;

	private final FixtureMonkey entityFixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.build();

	private Restaurant createTestRestaurant(RestaurantCategory category, String name) {
		Restaurant restaurant = entityFixtureMonkey.giveMeBuilder(Restaurant.class)
			.set("id", null)
			.set("restaurantCategory", category)
			.set("name", name)
			.set("details", name + " 설명")
			.set("address", DEFAULT_TEST_ADDRESS)
			.setNull("reviews")
			.setNull("menus")
			.setNull("favoriteRestaurants")
			.setNull("businessHours")
			.sample();
		return restaurantRepository.save(restaurant);
	}

	@Nested
	class 전체_식당_조회_테스트 {

		@BeforeEach
		void setUp() {
			한식_식당 = createTestRestaurant(RestaurantCategory.한식, TEST_KOREAN_RESTAURANT_NAME);
			중식_식당 = createTestRestaurant(RestaurantCategory.중식, TEST_CHINESE_RESTAURANT_NAME);
			양식_식당 = createTestRestaurant(RestaurantCategory.양식, TEST_WESTERN_RESTAURANT_NAME);
		}

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
				() -> assertThat(restaurantIds).contains(한식_식당.getId(), 중식_식당.getId(), 양식_식당.getId())
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

		@BeforeEach
		void setUp() {
			한식_식당 = createTestRestaurant(RestaurantCategory.한식, TEST_KOREAN_RESTAURANT_NAME);
			중식_식당 = createTestRestaurant(RestaurantCategory.중식, TEST_CHINESE_RESTAURANT_NAME);
			양식_식당 = createTestRestaurant(RestaurantCategory.양식, TEST_WESTERN_RESTAURANT_NAME);
		}

		@Test
		void 유효한_ID로_식당을_성공적으로_조회한다() {
			// when
			RestaurantFindResponse response = restaurantQueryService.getRestaurant(한식_식당.getId());

			// then
			assertAll(
				() -> assertThat(response).isNotNull(),
				() -> assertThat(response.id()).isEqualTo(한식_식당.getId()),
				() -> assertThat(response.name()).isEqualTo(한식_식당.getName()),
				() -> assertThat(response.details()).isEqualTo(한식_식당.getDetails()),
				() -> assertThat(response.address()).isEqualTo(한식_식당.getAddress()),
				() -> assertThat(response.categoryName()).isEqualTo(한식_식당.getRestaurantCategory().name())
			);
		}

		@Test
		void 존재하지_않는_ID로_조회시_CustomException이_발생한다() {
			// when & then
			assertThatThrownBy(() -> restaurantQueryService.getRestaurant(INVALID_RESTAURANT_ID))
				.isInstanceOf(CustomException.class)
				.hasMessage(RESTAURANT_NOT_FOUND.getMessage());
		}
	}

	@Nested
	class 카테고리별_식당_조회_테스트 {

		@BeforeEach
		void setUp() {
			한식_식당 = createTestRestaurant(RestaurantCategory.한식, TEST_KOREAN_RESTAURANT_NAME);
			중식_식당 = createTestRestaurant(RestaurantCategory.중식, TEST_CHINESE_RESTAURANT_NAME);
			양식_식당 = createTestRestaurant(RestaurantCategory.양식, TEST_WESTERN_RESTAURANT_NAME);
		}

		@Test
		void 특정_카테고리의_식당만_성공적으로_조회한다() {
			// when
			RestaurantFindListResponse response = restaurantQueryService.getRestaurantsByCategory("한식");

			// then
			assertAll(
				() -> assertThat(response).isNotNull(),
				() -> assertThat(response.restaurants()).isNotEmpty(),
				() -> assertThat(response.restaurants()).allSatisfy(restaurant -> assertThat(restaurant.categoryName()).isEqualTo("한식"))
			);
		}

		@Test
		void 존재하지_않는_카테고리로_조회시_CustomException이_발생한다() {
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

		@BeforeEach
		void setUp() {
			한식_식당 = createTestRestaurant(RestaurantCategory.한식, TEST_KOREAN_RESTAURANT_NAME);
			중식_식당 = createTestRestaurant(RestaurantCategory.중식, TEST_CHINESE_RESTAURANT_NAME);
			양식_식당 = createTestRestaurant(RestaurantCategory.양식, TEST_WESTERN_RESTAURANT_NAME);
		}

		@Test
		void 유효한_식당_ID로_카테고리를_성공적으로_조회한다() {
			// when
			CategoryFindResponse response = restaurantQueryService.getCategoryFromRestaurant(한식_식당.getId());

			// then
			assertAll(
				() -> assertThat(response).isNotNull(),
				() -> assertThat(response.category()).isEqualTo(한식_식당.getRestaurantCategory().name())
			);
		}

		@Test
		void 존재하지_않는_식당_ID로_카테고리_조회시_CustomException이_발생한다() {
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
