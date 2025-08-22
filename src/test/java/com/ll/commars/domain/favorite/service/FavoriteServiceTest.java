package com.ll.commars.domain.favorite.service;

import static com.ll.commars.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ll.commars.domain.favorite.dto.FavoriteCreateRequest;
import com.ll.commars.domain.favorite.dto.FavoriteCreateResponse;
import com.ll.commars.domain.favorite.dto.FavoriteFindListResponse;
import com.ll.commars.domain.favorite.dto.FavoriteRestaurantCreateRequest;
import com.ll.commars.domain.favorite.dto.FavoriteRestaurantsResponse;
import com.ll.commars.domain.favorite.entity.Favorite;
import com.ll.commars.domain.favorite.entity.FavoriteRestaurant;
import com.ll.commars.domain.favorite.fixture.FavoriteFixture;
import com.ll.commars.domain.favorite.repository.jpa.FavoriteRepository;
import com.ll.commars.domain.favorite.repository.jpa.FavoriteRestaurantRepository;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.fixture.MemberFixture;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.fixture.RestaurantFixture;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.global.annotation.IntegrationTest;
import com.ll.commars.global.exception.CustomException;

import com.navercorp.fixturemonkey.FixtureMonkey;

@IntegrationTest
@DisplayName("FavoriteService 테스트")
class FavoriteServiceTest {

	@Autowired
	private FavoriteService favoriteService;

	@Autowired
	private FavoriteRepository favoriteRepository;

	@Autowired
	private FavoriteRestaurantRepository favoriteRestaurantRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private FixtureMonkey fixtureMonkey;

	private FavoriteFixture favoriteFixture;

	private Member member1;
	private Member member2;
	private Restaurant restaurant1;
	private Restaurant restaurant2;

	@BeforeEach
	void setUp() {
		MemberFixture memberFixture = new MemberFixture(fixtureMonkey, memberRepository);
		RestaurantFixture restaurantFixture = new RestaurantFixture(fixtureMonkey, restaurantRepository);
		favoriteFixture = new FavoriteFixture(fixtureMonkey, favoriteRepository);

		member1 = memberFixture.이메일_사용자("user1@test.com", "password");
		member2 = memberFixture.이메일_사용자("user2@test.com", "password");
		restaurant1 = restaurantFixture.한식_식당();
		restaurant2 = restaurantFixture.중식_식당();
	}

	@Nested
	class 찜_리스트_생성_테스트 {

		@Test
		void 성공적으로_찜_리스트를_생성한다() {
			// given
			FavoriteCreateRequest request = new FavoriteCreateRequest("나의 맛집 리스트", true);

			// when
			FavoriteCreateResponse response = favoriteService.createFavorite(request, member1.getId());

			// then
			assertAll(
				() -> assertThat(response).isNotNull(),
				() -> assertThat(response.name()).isEqualTo("나의 맛집 리스트"),
				() -> assertThat(response.isPublic()).isTrue(),
				() -> assertThat(favoriteRepository.findById(response.id())).isPresent()
			);
		}

		@Test
		void 존재하지_않는_사용자로_찜_리스트_생성_시_예외가_발생한다() {
			// given
			long invalidMemberId = 9999L;
			FavoriteCreateRequest request = new FavoriteCreateRequest("나의 맛집 리스트", true);

			// when & then
			assertThatThrownBy(() -> favoriteService.createFavorite(request, invalidMemberId))
				.isInstanceOf(CustomException.class)
				.hasMessage(MEMBER_NOT_FOUND.getMessage());
		}

		@Test
		void 공개_여부를_설정하지_않으면_기본값_trre로_설정된다() {
			// given
			FavoriteCreateRequest request = new FavoriteCreateRequest("나의 맛집 리스트", null);

			// when
			FavoriteCreateResponse response = favoriteService.createFavorite(request, member1.getId());

			// then
			assertAll(
				() -> assertThat(response).isNotNull(),
				() -> assertThat(response.isPublic()).isTrue(),
				() -> assertThat(favoriteRepository.findById(response.id())).isPresent()
			);
		}
	}

	@Nested
	class 찜_리스트_조회_테스트 {

		@Test
		void 사용자의_모든_찜_리스트를_성공적으로_조회한다() {
			// given
			favoriteFixture.찜_리스트(member1, "강남 맛집", true);
			favoriteFixture.찜_리스트(member1, "홍대 카페", false);

			// when
			FavoriteFindListResponse response = favoriteService.getFavorites(member1.getId());

			// then
			assertThat(response.favoriteList()).hasSize(2);
		}
	}

	@Nested
	class 찜_리스트_내_식당_조회_테스트 {

		private Favorite favorite;

		@BeforeEach
		void setUp() {
			favorite = favoriteFixture.찜_리스트(member1, "내 맛집", true, List.of(restaurant1, restaurant2));
		}

		@Test
		void 특정_찜_리스트에_포함된_모든_식당을_성공적으로_조회한다() {
			// when
			FavoriteRestaurantsResponse response = favoriteService.getFavoriteRestaurants(favorite.getId(),
				member1.getId());

			// then
			assertAll(
				() -> assertThat(response).isNotNull(),
				() -> assertThat(response.totalCount()).isEqualTo(2),
				() -> assertThat(response.restaurants()).extracting("id")
					.containsExactlyInAnyOrder(restaurant1.getId(), restaurant2.getId())
			);
		}

		@Test
		void 다른_사용자의_찜_리스트_조회_시_예외가_발생한다() {
			// when & then
			assertThatThrownBy(() -> favoriteService.getFavoriteRestaurants(favorite.getId(), member2.getId()))
				.isInstanceOf(CustomException.class)
				.hasMessage(FAVORITE_FORBIDDEN.getMessage());
		}
	}

	@Nested
	class 찜_리스트_삭제_테스트 {

		private Favorite favorite;

		@BeforeEach
		void setUp() {
			favorite = favoriteFixture.찜_리스트(member1, "삭제할 리스트", true);
		}

		@Test
		void 성공적으로_찜_리스트를_삭제한다() {
			// when
			favoriteService.deleteFavorite(favorite.getId(), member1.getId());

			// then
			assertThat(favoriteRepository.findById(favorite.getId())).isEmpty();
		}

		@Test
		void 다른_사용자의_찜_리스트_삭제_시_예외가_발생한다() {
			// when & then
			assertThatThrownBy(() -> favoriteService.deleteFavorite(favorite.getId(), member2.getId()))
				.isInstanceOf(CustomException.class)
				.hasMessage(FAVORITE_FORBIDDEN.getMessage());
		}
	}

	@Nested
	class 찜_리스트에_식당_추가_테스트 {

		private Favorite favorite;

		@BeforeEach
		void setUp() {
			favorite = favoriteFixture.찜_리스트(member1, "내 맛집", true);
		}

		@Test
		void 성공적으로_찜_리스트에_식당을_추가한다() {
			// given
			FavoriteRestaurantCreateRequest request = new FavoriteRestaurantCreateRequest(favorite.getId());

			// when
			favoriteService.createFavoriteRestaurant(restaurant1.getId(), member1.getId(), request);

			// then
			boolean isExist = favoriteRestaurantRepository.existsByFavoriteAndRestaurant(favorite, restaurant1);
			assertThat(isExist).isTrue();
		}

		@Test
		void 이미_추가된_식당을_다시_추가하면_예외가_발생한다() {
			// given
			favoriteRestaurantRepository.save(new FavoriteRestaurant(favorite, restaurant1));
			FavoriteRestaurantCreateRequest request = new FavoriteRestaurantCreateRequest(favorite.getId());

			// when & then
			assertThatThrownBy(() -> favoriteService.createFavoriteRestaurant(restaurant1.getId(), member1.getId(), request))
				.isInstanceOf(CustomException.class)
				.hasMessage(FAVORITE_RESTAURANT_ALREADY_EXISTS.getMessage());
		}
	}

	@Nested
	class 찜_리스트에서_식당_삭제_테스트 {

		private Favorite favorite1;
		private Favorite favorite2;

		@BeforeEach
		void setUp() {
			favorite1 = favoriteFixture.찜_리스트(member1, "내 맛집 1", true, List.of(restaurant1, restaurant2));
			favorite2 = favoriteFixture.찜_리스트(member1, "내 맛집 2", true, List.of(restaurant1));
		}

		@Test
		void 사용자의_모든_찜_리스트에서_특정_식당을_성공적으로_삭제한다() {
			// when
			favoriteService.deleteFavoriteRestaurant(restaurant1.getId(), member1.getId());

			// then
			boolean isExistInFavorite1 = favoriteRestaurantRepository.existsByFavoriteAndRestaurant(favorite1, restaurant1);
			boolean isExistInFavorite2 = favoriteRestaurantRepository.existsByFavoriteAndRestaurant(favorite2, restaurant1);
			boolean otherRestaurantExists = favoriteRestaurantRepository.existsByFavoriteAndRestaurant(favorite1, restaurant2);

			assertAll(
				() -> assertThat(isExistInFavorite1).isFalse(),
				() -> assertThat(isExistInFavorite2).isFalse(),
				() -> assertThat(otherRestaurantExists).isTrue()
			);
		}

		@Test
		void 존재하지_않는_식당_삭제_시_예외가_발생한다() {
			// given
			long invalidRestaurantId = 9999L;

			// when & then
			assertThatThrownBy(() -> favoriteService.deleteFavoriteRestaurant(invalidRestaurantId, member1.getId()))
				.isInstanceOf(CustomException.class)
				.hasMessage(RESTAURANT_NOT_FOUND.getMessage());
		}

		@Test
		void 다른_사용자의_찜_리스트에_있는_식당은_삭제되지_않는다() {
			// given
			Favorite otherMemberFavorite = favoriteFixture.찜_리스트(member2, "남의 맛집", true);
			favoriteRestaurantRepository.save(new FavoriteRestaurant(otherMemberFavorite, restaurant1));

			// when
			favoriteService.deleteFavoriteRestaurant(restaurant1.getId(), member1.getId());

			// then
			boolean isExistForMember1 = favoriteRestaurantRepository.existsByFavoriteAndRestaurant(favorite1, restaurant1);
			boolean isExistForOtherMember = favoriteRestaurantRepository.existsByFavoriteAndRestaurant(otherMemberFavorite, restaurant1);

			assertAll(
				() -> assertThat(isExistForMember1).isFalse(),
				() -> assertThat(isExistForOtherMember).isTrue()
			);
		}
	}
}
