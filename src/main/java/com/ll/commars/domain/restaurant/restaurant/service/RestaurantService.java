package com.ll.commars.domain.restaurant.restaurant.service;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.RestaurantRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public Restaurant write(
            String name,
            String details,
            Double averageRate,
            String imageUrl,
            String contact,
            String address,
            Double lat,
            Double lng,
            Boolean runningState,
            String summarizedReview
    ) {
        Restaurant restaurant = Restaurant.builder()
                .name(name)
                .details(details)
                .averageRate(averageRate)
                .imageUrl(imageUrl)
                .contact(contact)
                .address(address)
                .lat(lat)
                .lng(lng)
                .runningState(runningState)
                .summarizedReview(summarizedReview)
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
