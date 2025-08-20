package com.ll.commars.domain.restaurant.restaurant.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantNearByRequest;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantSearchRequest;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantSearchResponse;
import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantCategory;
import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantDoc;
import com.ll.commars.domain.restaurant.restaurant.repository.elasticsearch.RestaurantDocRepository;
import com.ll.commars.global.annotation.IntegrationTest;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

@IntegrationTest
@DisplayName("RestaurantDocService 테스트")
public class RestaurantDocServiceTest {

	private static final double GANGNAM_LAT = 37.4964;
	private static final double GANGNAM_LON = 127.0283;
	private static final double PANGYO_LAT = 37.3947;
	private static final double PANGYO_LON = 127.1112;
	private static final double HONGDAE_LAT = 37.5574;
	private static final double HONGDAE_LON = 126.9269;
	private static final double SINNONHYEON_LAT = 37.5048;
	private static final double SINNONHYEON_LON = 127.0253;
	private static final String DEFAULT_DESCRIPTION = "설명";

	@Autowired
	private RestaurantDocService restaurantDocService;

	@Autowired
	private RestaurantDocRepository restaurantDocRepository;

	private static final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.build();

	@AfterEach
	void tearDown() {
		restaurantDocRepository.deleteAll();
	}

	private RestaurantDoc createRestaurantDoc(String name, String details, RestaurantCategory category, double lat,
		double lon, double averageRate) {
		return fixtureMonkey.giveMeBuilder(RestaurantDoc.class)
			.set("id", UUID.randomUUID().toString())
			.set("name", name)
			.set("details", details)
			.set("restaurantCategory", category)
			.set("location", new GeoPoint(lat, lon))
			.set("averageRate", averageRate)
			.sample();
	}

	@Nested
	class 식당_검색_테스트 {

		@BeforeEach
		void setUp() {
			restaurantDocRepository.saveAll(List.of(
				createRestaurantDoc("강남 교자", "맛있는 교자 전문점", RestaurantCategory.한식, GANGNAM_LAT, GANGNAM_LON, 4.5),
				createRestaurantDoc("판교 카츠", "바삭한 돈카츠", RestaurantCategory.일식, PANGYO_LAT, PANGYO_LON, 4.8),
				createRestaurantDoc("홍대 짜장면", "맛있는 짜장면", RestaurantCategory.중식, HONGDAE_LAT, HONGDAE_LON, 4.2)
			));
		}

		@Test
		void 키워드와_위치_기반으로_식당을_성공적으로_검색한다() {
			// given
			// 강남역과 신논현역은 1km 이내
			RestaurantSearchRequest request = new RestaurantSearchRequest("교자", SINNONHYEON_LAT, SINNONHYEON_LON,
				1000.0);

			// when
			RestaurantSearchResponse response = restaurantDocService.searchRestaurants(request);

			// then
			assertAll(
				() -> assertThat(response.restaurants()).hasSize(1),
				() -> assertThat(response.restaurants().get(0).name()).isEqualTo("강남 교자")
			);
		}

		@Test
		void 검색_결과가_없으면_빈_목록을_반환한다() {
			// given
			RestaurantSearchRequest request = new RestaurantSearchRequest("피자", GANGNAM_LAT, GANGNAM_LON, 1000.0);

			// when
			RestaurantSearchResponse response = restaurantDocService.searchRestaurants(request);

			// then
			assertThat(response.restaurants()).isEmpty();
		}

		@Test
		void 지정된_거리_밖의_식당은_검색_결과에서_제외된다() {
			// given
			// 강남역과 판교역은 5km 이외
			RestaurantSearchRequest request = new RestaurantSearchRequest("카츠", GANGNAM_LAT, GANGNAM_LON, 5000.0);

			// when
			RestaurantSearchResponse response = restaurantDocService.searchRestaurants(request);

			// then
			assertThat(response.restaurants()).isEmpty();
		}
	}

	@Nested
	class 식당_평점_정렬_테스트 {

		@BeforeEach
		void setUp() {
			restaurantDocRepository.saveAll(List.of(
				createRestaurantDoc("식당A", "설명", RestaurantCategory.한식, 37.1, 127.1, 4.5),
				createRestaurantDoc("식당B", "설명", RestaurantCategory.일식, 37.2, 127.2, 4.8),
				createRestaurantDoc("식당C", "설명", RestaurantCategory.중식, 37.3, 127.3, 3.9),
				createRestaurantDoc("식당D", "설명", RestaurantCategory.양식, 37.4, 127.4, 4.9),
				createRestaurantDoc("식당E", "설명", RestaurantCategory.한식, 37.5, 127.5, 4.2),
				createRestaurantDoc("식당F", "설명", RestaurantCategory.한식, 37.6, 127.6, 3.5)
			));
		}

		@Test
		void 평점_순으로_상위_5개_식당을_성공적으로_조회한다() {
			// when
			RestaurantSearchResponse response = restaurantDocService.sortByRate();

			// then
			List<RestaurantSearchResponse.RestaurantSearchInfo> restaurants = response.restaurants();

			assertAll(
				() -> assertThat(restaurants).hasSize(5),
				() -> assertThat(restaurants.get(0).name()).isEqualTo("식당D"),
				() -> assertThat(restaurants.get(1).name()).isEqualTo("식당B"),
				() -> assertThat(restaurants.get(2).name()).isEqualTo("식당A"),
				() -> assertThat(restaurants.get(3).name()).isEqualTo("식당E"),
				() -> assertThat(restaurants.get(4).name()).isEqualTo("식당C")
			);
		}

		@Test
		void 식당이_없을_경우_빈_목록을_반환한다() {
			// given
			restaurantDocRepository.deleteAll();

			// when
			RestaurantSearchResponse response = restaurantDocService.sortByRate();

			// then
			assertThat(response.restaurants()).isEmpty();
		}
	}

	@Nested
	class 근처_식당_검색_테스트 {

		private RestaurantDoc restaurantNear;
		private RestaurantDoc restaurantMid;
		private RestaurantDoc restaurantFar;

		@BeforeEach
		void setUp() {
			// 기준점: 강남역
			restaurantNear = createRestaurantDoc("가까운 식당", DEFAULT_DESCRIPTION, RestaurantCategory.한식, 37.498, 127.027,
				4.0); // 약 200m
			restaurantMid = createRestaurantDoc("중간 식당", DEFAULT_DESCRIPTION, RestaurantCategory.일식, 37.500, 127.030,
				4.5); // 약 400m
			restaurantFar = createRestaurantDoc("먼 식당", DEFAULT_DESCRIPTION, RestaurantCategory.중식, 37.505, 127.032,
				5.0); // 약 1km
			restaurantDocRepository.saveAll(List.of(restaurantFar, restaurantNear, restaurantMid));
		}

		@Test
		void 주변_식당을_거리순으로_성공적으로_조회한다() {
			// given
			RestaurantNearByRequest request = new RestaurantNearByRequest(GANGNAM_LAT, GANGNAM_LON, 500.0);

			// when
			RestaurantSearchResponse response = restaurantDocService.getNearbyRestaurants(request);

			// then
			List<RestaurantSearchResponse.RestaurantSearchInfo> restaurants = response.restaurants();

			assertAll(
				() -> assertThat(restaurants).hasSize(2),
				() -> assertThat(restaurants.get(0).name()).isEqualTo(restaurantNear.getName()),
				() -> assertThat(restaurants.get(1).name()).isEqualTo(restaurantMid.getName())
			);
		}

		@Test
		void 주변에_식당이_없을_경우_빈_목록을_반환한다() {
			// given
			RestaurantNearByRequest request = new RestaurantNearByRequest(GANGNAM_LAT, GANGNAM_LON, 50.0); // 50m 이내

			// when
			RestaurantSearchResponse response = restaurantDocService.getNearbyRestaurants(request);

			// then
			assertThat(response.restaurants()).isEmpty();
		}
	}
}
