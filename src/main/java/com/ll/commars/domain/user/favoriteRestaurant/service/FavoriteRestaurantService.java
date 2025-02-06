package com.ll.commars.domain.user.favoriteRestaurant.service;

import com.ll.commars.domain.user.favoriteRestaurant.repository.FavoriteRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteRestaurantService {
    private final FavoriteRestaurantRepository favoriteRestaurantRepository;

    public void truncate() {
        favoriteRestaurantRepository.deleteAll();
    }
}
