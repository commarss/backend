package com.ll.commars.domain.restaurant.menu.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.restaurant.menu.dto.MenuBulkCreateRequest;
import com.ll.commars.domain.restaurant.menu.dto.MenuBulkCreateResponse;
import com.ll.commars.domain.restaurant.menu.dto.MenuFindResponse;
import com.ll.commars.domain.restaurant.menu.dto.MenuUpdateRequest;
import com.ll.commars.domain.restaurant.menu.entity.Menu;
import com.ll.commars.domain.restaurant.menu.repository.jpa.MenuRepository;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {

	private final RestaurantRepository restaurantRepository;
	private final MenuRepository menuRepository;

	@Transactional
	public MenuBulkCreateResponse createMenu(
		MenuBulkCreateRequest request) {
		Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
			.orElseThrow(() -> new CustomException(RESTAURANT_NOT_FOUND));

		List<Menu> menus = request.menuCreateRequests().stream()
			.map(menuDto -> new Menu(
				menuDto.name(),
				menuDto.imageUrl(),
				menuDto.price(),
				restaurant
			))
			.toList();

		List<Menu> savedMenus = menuRepository.saveAll(menus);

		return MenuBulkCreateResponse.from(savedMenus);
	}

	@Transactional(readOnly = true)
	public MenuFindResponse getMenu(Long menuId) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new CustomException(MENU_NOT_FOUND));

		return MenuFindResponse.from(menu);
	}

	@Transactional
	public void updateMenu(Long menuId, MenuUpdateRequest request) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new CustomException(MENU_NOT_FOUND));

		menu.update(request.name(), request.imageUrl(), request.price());
	}

	@Transactional
	public void deleteMenu(Long menuId) {
		menuRepository.findById(menuId)
			.orElseThrow(() -> new CustomException(MENU_NOT_FOUND));

		menuRepository.deleteById(menuId);
	}
}
