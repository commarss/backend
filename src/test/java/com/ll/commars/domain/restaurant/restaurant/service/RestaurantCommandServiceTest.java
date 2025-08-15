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

    private final FixtureMonkey entityFixtureMonkey = FixtureMonkey.builder()
        .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
        .build();

    private final FixtureMonkey dtoFixtureMonkey = FixtureMonkey.builder()
        .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
        .build();

    @BeforeEach
    void setUp() {
        한식_식당 = createTestRestaurant(RestaurantCategory.한식, "테스트 한식당");
        중식_식당 = createTestRestaurant(RestaurantCategory.중식, "테스트 중식당");
    }

    private Restaurant createTestRestaurant(RestaurantCategory category, String name) {
        Restaurant restaurant = entityFixtureMonkey.giveMeBuilder(Restaurant.class)
            .set("id", null)
            .set("restaurantCategory", category)
            .set("name", name)
            .set("details", name + " 설명")
            .set("address", "서울시 강남구")
            .set("lat", 37.0)
            .set("lon", 128.0)
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
                .set("name", "새로운 한식당")
                .set("details", "맛있는 한식당입니다")
                .set("address", "서울시 종로구")
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
                .set("address", "서울시 종로구")
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
            long invalidRestaurantId = 99999L;
            RestaurantUpdateRequest request = dtoFixtureMonkey.giveMeOne(RestaurantUpdateRequest.class);

            // when & then
            assertThatThrownBy(() -> restaurantCommandService.updateRestaurant(invalidRestaurantId, request))
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
            long invalidRestaurantId = 99999L;

            // when & then
            assertThatThrownBy(() -> restaurantCommandService.deleteRestaurant(invalidRestaurantId))
                .isInstanceOf(CustomException.class)
                .hasMessage(RESTAURANT_NOT_FOUND.getMessage());
        }
    }
}
