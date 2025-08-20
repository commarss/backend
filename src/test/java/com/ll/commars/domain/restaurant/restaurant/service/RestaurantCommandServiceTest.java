package com.ll.commars.domain.restaurant.restaurant.service;

import static com.ll.commars.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantCreateRequest;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantCreateResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantUpdateRequest;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantCategory;
import com.ll.commars.domain.restaurant.restaurant.fixture.RestaurantFixture;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
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
	private RestaurantFixture restaurantFixture;

	@Autowired
	private FixtureMonkey fixtureMonkey;

	private Restaurant koreanRestaurant;

	private Restaurant chineseRestaurant;
	private Restaurant restaurant;

	private static final long INVALID_RESTAURANT_ID = 99999L;

	@BeforeEach
	void setUp() {
		restaurantFixture = new RestaurantFixture(fixtureMonkey, restaurantRepository);

		koreanRestaurant = restaurantFixture.한식_식당();
		chineseRestaurant = restaurantFixture.중식_식당();
		restaurant = restaurantFixture.양식_식당();
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
			restaurantCommandService.updateRestaurant(restaurant.getId(), request);

			// then
			Restaurant updatedRestaurant = restaurantRepository.findById(restaurant.getId()).get();

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
}
