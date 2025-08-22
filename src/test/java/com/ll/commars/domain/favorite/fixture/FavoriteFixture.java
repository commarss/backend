package com.ll.commars.domain.favorite.fixture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.test.context.TestComponent;

import com.ll.commars.domain.favorite.entity.Favorite;
import com.ll.commars.domain.favorite.repository.jpa.FavoriteRepository;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;

import lombok.RequiredArgsConstructor;

import com.navercorp.fixturemonkey.FixtureMonkey;

@TestComponent
@RequiredArgsConstructor
public class FavoriteFixture {

	private final FixtureMonkey fixtureMonkey;
	private final FavoriteRepository favoriteRepository;

	public Favorite 찜_리스트(Member member, String name, boolean isPublic) {
		return 찜_리스트(member, name, isPublic, Collections.emptyList());
	}

	public Favorite 찜_리스트(Member member, String name, boolean isPublic, List<Restaurant> restaurants) {
		Favorite favorite = fixtureMonkey.giveMeBuilder(Favorite.class)
			.set("id", null)
			.set("member", member)
			.set("name", name)
			.set("isPublic", isPublic)
			.set("favoriteRestaurants", new ArrayList<>())
			.sample();

		if (restaurants != null && !restaurants.isEmpty()) {
			restaurants.forEach(favorite::addRestaurant);
		}

		return favoriteRepository.save(favorite);
	}
}
