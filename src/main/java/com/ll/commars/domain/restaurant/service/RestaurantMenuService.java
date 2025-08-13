package com.ll.commars.domain.restaurant.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.restaurant.dto.MenuCreateRequest;
import com.ll.commars.domain.restaurant.dto.MenuCreateResponse;
import com.ll.commars.domain.restaurant.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.entity.RestaurantMenu;
import com.ll.commars.domain.restaurant.repository.jpa.RestaurantMenuRepository;
import com.ll.commars.domain.restaurant.entity.Restaurant;
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

		RestaurantMenu restaurantMenu = new RestaurantMenu(request.menuName(), request.imageUrl(), request.price());
		restaurantMenu.setRestaurant(restaurant);

		return MenuCreateResponse.from(restaurantMenuRepository.save(restaurantMenu));
	}

	@Transactional
	public RestaurantMenuDto.MenuWriteResponse modifyMenu(Long menuId, RestaurantMenuDto.MenuInfo request) {
		RestaurantMenu restaurantMenu = restaurantMenuRepository.findById(menuId)
			.orElseThrow(() -> new IllegalArgumentException("Menu not found"));

		restaurantMenu.setName(request.getName());
		restaurantMenu.setPrice(request.getPrice());
		restaurantMenu.setImageUrl(request.getImageUrl());

		return RestaurantMenuDto.MenuWriteResponse.builder()
			.restaurantName(restaurantMenu.getRestaurant().getName())
			.name(restaurantMenu.getName())
			.price(restaurantMenu.getPrice())
			.build();
	}

	@Transactional
	public void deleteMenu(Long menuId) {
		restaurantMenuRepository.findById(menuId)
			.orElseThrow(() -> new IllegalArgumentException("Menu not found"));

		restaurantMenuRepository.deleteById(menuId);
	}
}
