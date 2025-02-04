package com.ll.commars.domain.restaurant.restaurant.service;

import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantDto;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

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

    public void truncate() {
        restaurantRepository.deleteAll();
    }

    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }
}
