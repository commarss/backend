package com.ll.commars.domain.restaurant.menu.service;

import com.ll.commars.domain.restaurant.menu.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.menu.entity.RestaurantMenu;
import com.ll.commars.domain.restaurant.menu.repository.RestaurantMenuRepository;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantMenuService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMenuRepository restaurantMenuRepository;

    @Transactional
    public RestaurantMenuDto.MenuWriteResponse write(
            Long restaurantId,
            RestaurantMenuDto.MenuInfo request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        RestaurantMenu restaurantMenu = RestaurantMenu.builder()
                .restaurant(restaurant)
                .name(request.getName())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .build();

        restaurantMenuRepository.save(restaurantMenu);

        return RestaurantMenuDto.MenuWriteResponse.builder()
                .restaurantName(restaurant.getName())
                .name(restaurantMenu.getName())
                .price(restaurantMenu.getPrice())
                .build();
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

    public void truncate() {
        restaurantMenuRepository.deleteAll();
    }
}
