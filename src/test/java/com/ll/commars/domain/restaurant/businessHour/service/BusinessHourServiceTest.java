package com.ll.commars.domain.restaurant.businessHour.service;

import static com.ll.commars.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourBulkCreateRequest;
import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourCreateRequest;
import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourUpdateRequest;
import com.ll.commars.domain.restaurant.businessHour.entity.BusinessHour;
import com.ll.commars.domain.restaurant.businessHour.fixture.BusinessHourFixture;
import com.ll.commars.domain.restaurant.businessHour.repository.jpa.BusinessHourRepository;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.fixture.RestaurantFixture;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.global.annotation.IntegrationTest;
import com.ll.commars.global.exception.CustomException;

import com.navercorp.fixturemonkey.FixtureMonkey;

@IntegrationTest
@DisplayName("BusinessHourService 테스트")
class BusinessHourServiceTest {

	@Autowired
	private BusinessHourService businessHourService;

	@Autowired
	private BusinessHourRepository businessHourRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private FixtureMonkey fixtureMonkey;

	private BusinessHourFixture businessHourFixture;

	private Restaurant restaurant;
	private static final long INVALID_ID = 99999L;

	@BeforeEach
	void setUp() {
		RestaurantFixture restaurantFixture = new RestaurantFixture(fixtureMonkey, restaurantRepository);
		this.businessHourFixture = new BusinessHourFixture(fixtureMonkey, businessHourRepository);
		this.restaurant = restaurantFixture.한식_식당();
	}

	@Nested
	class 영업시간_일괄_생성_테스트 {

		@Test
		void 성공적으로_영업시간을_일괄_생성한다() {
			// given
			BusinessHourCreateRequest monday = new BusinessHourCreateRequest(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(22, 0));
			BusinessHourCreateRequest tuesday = new BusinessHourCreateRequest(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(22, 0));
			BusinessHourBulkCreateRequest request = new BusinessHourBulkCreateRequest(restaurant.getId(), List.of(monday, tuesday));

			// when
			businessHourService.createBusinessHours(request);

			// then
			List<BusinessHour> hours = businessHourRepository.findAllByRestaurant(restaurant);
			assertAll(
				() -> assertThat(hours).hasSize(2),
				() -> assertThat(hours).extracting(BusinessHour::getDayOfWeek).contains(DayOfWeek.MONDAY, DayOfWeek.TUESDAY)
			);
		}

		@Test
		void 존재하지_않는_식당에_영업시간을_추가하면_예외가_발생한다() {
			// given
			BusinessHourCreateRequest monday = new BusinessHourCreateRequest(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(22, 0));
			BusinessHourBulkCreateRequest request = new BusinessHourBulkCreateRequest(INVALID_ID, List.of(monday));

			// when & then
			assertThatThrownBy(() -> businessHourService.createBusinessHours(request))
				.isInstanceOf(CustomException.class)
				.hasMessage(RESTAURANT_NOT_FOUND.getMessage());
		}
	}

	@Nested
	class 영업시간_수정_테스트 {

		private BusinessHour businessHour;

		@BeforeEach
		void setUp() {
			businessHour = businessHourFixture.영업시간(restaurant, DayOfWeek.WEDNESDAY, LocalTime.of(10, 0), LocalTime.of(20, 0));
		}

		@Test
		void 성공적으로_영업시간을_수정한다() {
			// given
			BusinessHourUpdateRequest request = new BusinessHourUpdateRequest(DayOfWeek.THURSDAY, LocalTime.of(11, 0), LocalTime.of(21, 0));

			// when
			businessHourService.updateBusinessHour(businessHour.getId(), request);

			// then
			BusinessHour updatedHour = businessHourRepository.findById(businessHour.getId()).get();
			assertAll(
				() -> assertThat(updatedHour.getDayOfWeek()).isEqualTo(request.dayOfWeek()),
				() -> assertThat(updatedHour.getOpenTime()).isEqualTo(request.openTime()),
				() -> assertThat(updatedHour.getCloseTime()).isEqualTo(request.closeTime())
			);
		}

		@Test
		void 존재하지_않는_영업시간을_수정하면_예외가_발생한다() {
			// given
			BusinessHourUpdateRequest request = new BusinessHourUpdateRequest(DayOfWeek.FRIDAY, LocalTime.of(10, 0), LocalTime.of(20, 0));

			// when & then
			assertThatThrownBy(() -> businessHourService.updateBusinessHour(INVALID_ID, request))
				.isInstanceOf(CustomException.class)
				.hasMessage(BUSINESS_HOUR_NOT_FOUND.getMessage());
		}
	}

	@Nested
	class 영업시간_삭제_테스트 {

		private BusinessHour businessHour;

		@BeforeEach
		void setUp() {
			businessHour = businessHourFixture.영업시간(restaurant, DayOfWeek.SATURDAY, LocalTime.of(12, 0), LocalTime.of(23, 0));
		}

		@Test
		void 성공적으로_영업시간을_삭제한다() {
			// when
			businessHourService.deleteBusinessHour(businessHour.getId());

			// then
			assertThat(businessHourRepository.findById(businessHour.getId())).isEmpty();
		}

		@Test
		void 존재하지_않는_영업시간을_삭제하면_예외가_발생한다() {
			// when & then
			assertThatThrownBy(() -> businessHourService.deleteBusinessHour(INVALID_ID))
				.isInstanceOf(CustomException.class)
				.hasMessage(BUSINESS_HOUR_NOT_FOUND.getMessage());
		}
	}
}
