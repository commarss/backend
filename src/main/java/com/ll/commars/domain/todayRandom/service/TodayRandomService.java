package com.ll.commars.domain.todayRandom.service;

import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantSummaryDTO;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.RestaurantRepository;
import com.ll.commars.domain.user.favorite.entity.Favorite;
import com.ll.commars.domain.user.favorite.repository.FavoriteRepository;
import com.ll.commars.domain.user.favoriteRestaurant.entity.FavoriteRestaurant;
import com.ll.commars.domain.user.favoriteRestaurant.repository.FavoriteRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class TodayRandomService {
    private final RestaurantRepository restaurantRepository;
    private final FavoriteRestaurantRepository favoriteRestaurantRepository;
    private final FavoriteRepository favoriteRepository;

    @Autowired
    public TodayRandomService(RestaurantRepository restaurantRepository,
                              FavoriteRestaurantRepository favoriteRestaurantRepository,
                              FavoriteRepository favoriteRepository) {
        this.restaurantRepository = restaurantRepository;
        this.favoriteRestaurantRepository = favoriteRestaurantRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional(readOnly = true) // Lazy Loading 방지
    public List<RestaurantSummaryDTO> getRandomRestaurants(double lat, double lng, Long userId) {
        // 1. 유저의 찜 목록 가져오기
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        Set<Long> favoriteIds = favorites.stream()
                .map(Favorite::getId)
                .collect(Collectors.toSet());

        // 2. 찜 목록에 해당하는 식당 가져오기
        List<FavoriteRestaurant> favoriteRestaurants = favoriteRestaurantRepository.findByFavoriteIdIn(favoriteIds);
        Set<Long> favoriteRestaurantIds = favoriteRestaurants.stream()
                .map(fr -> fr.getRestaurant().getId())
                .collect(Collectors.toSet());

        // 3. 거리 내의 식당 목록 가져오기
        List<Restaurant> nearbyRestaurants = restaurantRepository.findRestaurantsWithinRadius(lat, lng);

        // 4. 반경 내 찜한 식당 필터링
        List<Restaurant> filteredRestaurants = nearbyRestaurants.stream()
                .filter(r -> favoriteRestaurantIds.contains(r.getId()))
                .collect(Collectors.toList());

        // 5. 랜덤하게 5개 선택 후 DTO 변환
        Collections.shuffle(filteredRestaurants);
        return filteredRestaurants.stream()
                .limit(5)
                .map(RestaurantSummaryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /*@Transactional(readOnly = true) //랜덤으로 1개 선택
    public Optional<RestaurantSummaryDTO> getRandomRestaurantDetails(double lat, double lng, Long userId) {
        List<RestaurantSummaryDTO> randomRestaurants = getRandomRestaurants(lat, lng, userId);

        if (randomRestaurants.isEmpty()) {
            return Optional.empty();
        }

        // 랜덤으로 1개 선택
        return Optional.of(randomRestaurants.get(new Random().nextInt(randomRestaurants.size())));
    }*/

    @Transactional(readOnly = true)
    public Optional<RestaurantSummaryDTO> getRestaurantDetails(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .map(RestaurantSummaryDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public List<RestaurantSummaryDTO> getnotuserRandomRestaurants(double lat, double lng) {
        // 반경 2km 내의 식당 목록 가져오기
        List<Restaurant> nearbyRestaurants = restaurantRepository.findRestaurantsWithinRadius(lat, lng);

        // 식당이 5개 이하라면 그대로 반환
        if (nearbyRestaurants.size() <= 5) {
            return nearbyRestaurants.stream()
                    .map(RestaurantSummaryDTO::fromEntity)
                    .collect(Collectors.toList());
        }

        // 무작위로 5개 선택
        Collections.shuffle(nearbyRestaurants);
        return nearbyRestaurants.stream()
                .limit(5)
                .map(RestaurantSummaryDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
