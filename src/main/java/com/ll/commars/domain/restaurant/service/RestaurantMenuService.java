package com.ll.commars.domain.restaurant.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.restaurant.dto.MenuCreateRequest;
import com.ll.commars.domain.restaurant.dto.MenuCreateResponse;
import com.ll.commars.domain.restaurant.dto.MenuUpdateRequest;
import com.ll.commars.domain.restaurant.dto.MenuUpdateResponse;
import com.ll.commars.domain.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.entity.Menu;
import com.ll.commars.domain.restaurant.repository.jpa.RestaurantMenuRepository;
import com.ll.commars.domain.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantMenuService {

	private final RestaurantRepository restaurantRepository;
	private final RestaurantMenuRepository restaurantMenuRepository;

	@Transactional
	public MenuCreateResponse createMenu(
		Long restaurantId,
		MenuCreateRequest request) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new CustomException(RESTAURANT_NOT_FOUND));

		Menu menu = new Menu(request.menuName(), request.imageUrl(), request.price());
		menu.setRestaurant(restaurant);

		return MenuCreateResponse.from(restaurantMenuRepository.save(menu));
	}

	@Transactional
	public MenuUpdateResponse updateMenu(Long menuId, MenuUpdateRequest request) {
		Menu menu = restaurantMenuRepository.findById(menuId)
			.orElseThrow(() -> new CustomException(MENU_NOT_FOUND));

		menu.update(request.menuName(), request.imageUrl(), request.price());

		return MenuUpdateResponse.from(menu);
	}

	@Transactional
	public void deleteMenu(Long menuId) {
		restaurantMenuRepository.findById(menuId)
			.orElseThrow(() -> new CustomException(MENU_NOT_FOUND));

		restaurantMenuRepository.deleteById(menuId);
	}
}
