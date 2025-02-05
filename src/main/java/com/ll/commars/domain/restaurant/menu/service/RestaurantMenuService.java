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
    public RestaurantMenuDto.RestaurantMenuWriteResponse write(
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

        return RestaurantMenuDto.RestaurantMenuWriteResponse.builder()
                .restaurantName(restaurant.getName())
                .name(restaurantMenu.getName())
                .price(restaurantMenu.getPrice())
                .build();
    }
}
