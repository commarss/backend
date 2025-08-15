package com.ll.commars.domain.restaurant.restaurant.service;

import static com.ll.commars.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

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
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.global.annotation.IntegrationTest;
import com.ll.commars.global.exception.CustomException;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

@IntegrationTest
@DisplayName("RestaurantCommandService 테스트")
public class RestaurantCommandServiceTest {

    @Autowired
    private RestaurantCommandService restaurantCommandService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private Restaurant 한식_식당;

    private Restaurant 중식_식당;

    private static final String TEST_KOREAN_RESTAURANT_NAME = "테스트 한식당";
    private static final String TEST_CHINESE_RESTAURANT_NAME = "테스트 중식당";
    private static final String DEFAULT_TEST_ADDRESS = "서울시 강남구";
    private static final long INVALID_RESTAURANT_ID = 99999L;

    private final FixtureMonkey entityFixtureMonkey = FixtureMonkey.builder()
        .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
        .build();

    private final FixtureMonkey dtoFixtureMonkey = FixtureMonkey.builder()
        .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
        .build();

    @BeforeEach
    void setUp() {
        한식_식당 = createTestRestaurant(RestaurantCategory.한식, TEST_KOREAN_RESTAURANT_NAME);
        중식_식당 = createTestRestaurant(RestaurantCategory.중식, TEST_CHINESE_RESTAURANT_NAME);
    }

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
    class 레스토랑_생성_테스트 {

        @Test
        void 성공적으로_레스토랑을_생성한다() {
            // given
            RestaurantCreateRequest request = dtoFixtureMonkey.giveMeBuilder(RestaurantCreateRequest.class)
                .set("name", TEST_KOREAN_RESTAURANT_NAME)
                .set("details", "맛있는 한식당입니다")
                .set("address", DEFAULT_TEST_ADDRESS)
                .set("category", "한식")
                .set("imageUrl", "https://example.com/image.jpg")
                .set("contact", "02-123-4567")
                .sample();

            // when
            RestaurantCreateResponse response = restaurantCommandService.createRestaurant(request);

            // then
            assertThat(response).isNotNull();
            assertThat(response.id()).isNotNull();
            assertThat(restaurantRepository.findById(response.id())).isPresent();
        }
    }

    @Nested
    class 레스토랑_수정_테스트 {

        @Test
        void 성공적으로_레스토랑_정보를_수정한다() {
            // given
            RestaurantUpdateRequest request = dtoFixtureMonkey.giveMeBuilder(RestaurantUpdateRequest.class)
                .set("name", "수정된 맛집")
                .set("details", "수정된 식당입니다")
                .set("address", DEFAULT_TEST_ADDRESS)
                .set("category", "일식")
                .set("imageUrl", "https://example.com/updated-image.jpg")
                .set("contact", "02-987-6543")
                .sample();

            // when
            restaurantCommandService.updateRestaurant(한식_식당.getId(), request);

            // then
            Restaurant updatedRestaurant = restaurantRepository.findById(한식_식당.getId()).get();
            assertThat(updatedRestaurant.getName()).isEqualTo(request.name());
            assertThat(updatedRestaurant.getRestaurantCategory()).isEqualTo(RestaurantCategory.일식);
        }

        @Test
        void 존재하지_않는_레스토랑을_수정하면_CustomException이_발생한다() {
            // given
            RestaurantUpdateRequest request = dtoFixtureMonkey.giveMeOne(RestaurantUpdateRequest.class);

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
            restaurantCommandService.deleteRestaurant(중식_식당.getId());

            // then
            assertThat(restaurantRepository.findById(중식_식당.getId())).isEmpty();
        }

        @Test
        void 존재하지_않는_레스토랑을_삭제하면_CustomException이_발생한다() {
            // given

            // when & then
            assertThatThrownBy(() -> restaurantCommandService.deleteRestaurant(INVALID_RESTAURANT_ID))
                .isInstanceOf(CustomException.class)
                .hasMessage(RESTAURANT_NOT_FOUND.getMessage());
        }
    }
}
