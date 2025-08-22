package com.ll.commars.domain.restaurant.menu.fixture;

import org.springframework.boot.test.context.TestComponent;

import com.ll.commars.domain.restaurant.menu.entity.Menu;
import com.ll.commars.domain.restaurant.menu.repository.jpa.MenuRepository;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;

import lombok.RequiredArgsConstructor;

import com.navercorp.fixturemonkey.FixtureMonkey;

@TestComponent
@RequiredArgsConstructor
public class MenuFixture {

	private final FixtureMonkey fixtureMonkey;
	private final MenuRepository menuRepository;

	public Menu 메뉴(Restaurant restaurant, String name, Integer price, String imageUrl) {
		Menu menu = fixtureMonkey.giveMeBuilder(Menu.class)
			.set("id", null)
			.set("restaurant", restaurant)
			.set("name", name)
			.set("price", price)
			.set("imageUrl", imageUrl)
			.sample();

		return menuRepository.save(menu);
	}
}
