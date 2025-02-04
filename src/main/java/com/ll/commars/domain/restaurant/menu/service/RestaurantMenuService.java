package com.ll.commars.domain.restaurant.menu.service;

import com.ll.commars.domain.restaurant.menu.dto.RestaurantMenuDTO;
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
    public RestaurantMenuDTO write(String name, Integer price, String imageUrl, String restaurantName) {
        Restaurant restaurant = restaurantRepository.findByName(restaurantName)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        RestaurantMenu restaurantMenu = RestaurantMenu.builder()
                .name(name)
                .price(price)
                .imageUrl(imageUrl)
                .restaurant(restaurant)
                .build();

        restaurantMenuRepository.save(restaurantMenu);

        return RestaurantMenuDTO.from(restaurantMenu);
    }
}
