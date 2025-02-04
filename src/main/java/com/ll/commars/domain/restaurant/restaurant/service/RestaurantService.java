package com.ll.commars.domain.restaurant.restaurant.service;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public Restaurant write(String name, String details, Double averageRate) {
        Restaurant restaurant = Restaurant.builder()
                .name(name)
                .details(details)
                .averageRate(averageRate)
                .build();

        return restaurantRepository.save(restaurant);
    }

    public void truncate() {
        restaurantRepository.deleteAll();
    }

    public String getRestaurantDetail(String name) {
        Restaurant restaurant = restaurantRepository.findByName(name);
        return restaurant.getDetails();
    }
}
