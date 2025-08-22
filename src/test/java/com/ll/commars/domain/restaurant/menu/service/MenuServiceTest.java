package com.ll.commars.domain.restaurant.menu.service;

import static com.ll.commars.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ll.commars.domain.restaurant.menu.dto.MenuBulkCreateRequest;
import com.ll.commars.domain.restaurant.menu.dto.MenuBulkCreateResponse;
import com.ll.commars.domain.restaurant.menu.dto.MenuCreateRequest;
import com.ll.commars.domain.restaurant.menu.dto.MenuFindResponse;
import com.ll.commars.domain.restaurant.menu.dto.MenuUpdateRequest;
import com.ll.commars.domain.restaurant.menu.entity.Menu;
import com.ll.commars.domain.restaurant.menu.fixture.MenuFixture;
import com.ll.commars.domain.restaurant.menu.repository.jpa.MenuRepository;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.fixture.RestaurantFixture;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.global.annotation.IntegrationTest;
import com.ll.commars.global.exception.CustomException;

import com.navercorp.fixturemonkey.FixtureMonkey;

@IntegrationTest
@DisplayName("MenuService 테스트")
class MenuServiceTest {

	@Autowired
	private MenuService menuService;

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private FixtureMonkey fixtureMonkey;

	private MenuFixture menuFixture;

	private Restaurant restaurant;
	private static final long INVALID_ID = 99999L;

	@BeforeEach
	void setUp() {
		RestaurantFixture restaurantFixture = new RestaurantFixture(fixtureMonkey, restaurantRepository);
		this.menuFixture = new MenuFixture(fixtureMonkey, menuRepository);
		this.restaurant = restaurantFixture.한식_식당();
	}

	@Nested
	class 메뉴_일괄_생성_테스트 {

		@Test
		void 성공적으로_메뉴를_일괄_생성한다() {
			// given
			MenuCreateRequest menu1 = new MenuCreateRequest("김치찌개", "image1.url", 8000);
			MenuCreateRequest menu2 = new MenuCreateRequest("된장찌개", "image2.url", 8000);
			MenuBulkCreateRequest request = new MenuBulkCreateRequest(restaurant.getId(), List.of(menu1, menu2));

			// when
			MenuBulkCreateResponse response = menuService.createMenu(request);

			// then
			List<Menu> savedMenus = menuRepository.findAllById(response.createdMenuIds());
			assertAll(
				() -> assertThat(response.createdCount()).isEqualTo(2),
				() -> assertThat(savedMenus).hasSize(2),
				() -> assertThat(savedMenus).extracting(Menu::getName).contains("김치찌개", "된장찌개")
			);
		}

		@Test
		void 존재하지_않는_식당에_메뉴를_추가하면_예외가_발생한다() {
			// given
			MenuCreateRequest menu1 = new MenuCreateRequest("메뉴", "image.url", 10000);
			MenuBulkCreateRequest request = new MenuBulkCreateRequest(INVALID_ID, List.of(menu1));

			// when & then
			assertThatThrownBy(() -> menuService.createMenu(request))
				.isInstanceOf(CustomException.class)
				.hasMessage(RESTAURANT_NOT_FOUND.getMessage());
		}
	}

	@Nested
	class 메뉴_단건_조회_테스트 {

		private Menu menu;

		@BeforeEach
		void setUp() {
			menu = menuFixture.메뉴(restaurant, "테스트 메뉴", 12000, "image.url");
		}

		@Test
		void 성공적으로_메뉴를_조회한다() {
			// when
			MenuFindResponse response = menuService.getMenu(menu.getId());

			// then
			assertAll(
				() -> assertThat(response.id()).isEqualTo(menu.getId()),
				() -> assertThat(response.name()).isEqualTo(menu.getName()),
				() -> assertThat(response.price()).isEqualTo(menu.getPrice())
			);
		}

		@Test
		void 존재하지_않는_메뉴를_조회하면_예외가_발생한다() {
			// when & then
			assertThatThrownBy(() -> menuService.getMenu(INVALID_ID))
				.isInstanceOf(CustomException.class)
				.hasMessage(MENU_NOT_FOUND.getMessage());
		}
	}

	@Nested
	class 메뉴_수정_테스트 {

		private Menu menu;

		@BeforeEach
		void setUp() {
			menu = menuFixture.메뉴(restaurant, "원본 메뉴", 10000, "original.url");
		}

		@Test
		void 성공적으로_메뉴를_수정한다() {
			// given
			MenuUpdateRequest request = new MenuUpdateRequest("수정된 메뉴", "updated.url", 15000);

			// when
			menuService.updateMenu(menu.getId(), request);

			// then
			Menu updatedMenu = menuRepository.findById(menu.getId()).get();
			assertAll(
				() -> assertThat(updatedMenu.getName()).isEqualTo(request.name()),
				() -> assertThat(updatedMenu.getPrice()).isEqualTo(request.price()),
				() -> assertThat(updatedMenu.getImageUrl()).isEqualTo(request.imageUrl())
			);
		}

		@Test
		void 존재하지_않는_메뉴를_수정하면_예외가_발생한다() {
			// given
			MenuUpdateRequest request = new MenuUpdateRequest("수정된 메뉴", "updated.url", 15000);

			// when & then
			assertThatThrownBy(() -> menuService.updateMenu(INVALID_ID, request))
				.isInstanceOf(CustomException.class)
				.hasMessage(MENU_NOT_FOUND.getMessage());
		}
	}

	@Nested
	class 메뉴_삭제_테스트 {

		private Menu menu;

		@BeforeEach
		void setUp() {
			menu = menuFixture.메뉴(restaurant, "삭제될 메뉴", 9000, "delete.url");
		}

		@Test
		void 성공적으로_메뉴를_삭제한다() {
			// when
			menuService.deleteMenu(menu.getId());

			// then
			assertThat(menuRepository.findById(menu.getId())).isEmpty();
		}

		@Test
		void 존재하지_않는_메뉴를_삭제하면_예외가_발생한다() {
			// when & then
			assertThatThrownBy(() -> menuService.deleteMenu(INVALID_ID))
				.isInstanceOf(CustomException.class)
				.hasMessage(MENU_NOT_FOUND.getMessage());
		}
	}
}
