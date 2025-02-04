package com.ll.commars.domain.restaurant.restaurant.service;

import com.ll.commars.domain.restaurant.menu.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantDto;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public RestaurantDto.RestaurantWriteResponse write(
            RestaurantDto.RestaurantWriteRequest request
    ) {
        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .details(request.getDetails())
                .averageRate(request.getAverageRate())
                .imageUrl(request.getImageUrl())
                .contact(request.getContact())
                .address(request.getAddress())
                .lat(request.getLat())
                .lng(request.getLng())
                .runningState(request.getRunningState())
                .summarizedReview(request.getSummarizedReview())
                .build();

        restaurantRepository.save(restaurant);

        return RestaurantDto.RestaurantWriteResponse.builder()
                .name(request.getName())
                .build();
    }

    @Transactional
    public void truncate() {
        restaurantRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public RestaurantDto.RestaurantShowAllResponse getRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAllWithMenus();

        List<RestaurantDto.RestaurantInfo> restaurantInfos = restaurants.stream()
                .map(restaurant -> {
                    List<RestaurantMenuDto.MenuInfo> menuInfos = restaurant.getRestaurantMenus().stream()
                            .map(menu -> RestaurantMenuDto.MenuInfo.builder()
                                    .name(menu.getName())
                                    .price(menu.getPrice())
                                    .imageUrl(menu.getImageUrl())
                                    .build())
                            .collect(Collectors.toList());

                    return RestaurantDto.RestaurantInfo.builder()
                            .restaurantName(restaurant.getName())
                            .restaurantMenus(menuInfos)
                            .build();
                })
                .collect(Collectors.toList());

        return RestaurantDto.RestaurantShowAllResponse.builder()
                .restaurants(restaurantInfos)
                .build();
    }
}
