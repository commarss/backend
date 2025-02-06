package com.ll.commars.domain.user.favorite.service;

import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantDto;
import com.ll.commars.domain.user.favorite.dto.FavoriteDto;
import com.ll.commars.domain.user.favorite.entity.Favorite;
import com.ll.commars.domain.user.favorite.repository.FavoriteRepository;
import com.ll.commars.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    public int getFavoriteCount(String email) {
        return favoriteRepository.countByUserEmail(email);
    }

    public List<Favorite> getFavoritesByUser(User user) {
        return favoriteRepository.findByUserEmail(user.getEmail());
    }

    public FavoriteDto.FavoriteInfo toFavoriteInfo(Favorite favorite) {
        List<RestaurantDto.RestaurantInfo> restaurants = favorite.getFavoriteRestaurants().stream()
                .map(fr -> RestaurantDto.RestaurantInfo.builder()
                        .id(fr.getRestaurant().getId())
                        .name(fr.getRestaurant().getName())
                        .address(fr.getRestaurant().getAddress())
                        .build())
                .collect(Collectors.toList());

        return FavoriteDto.FavoriteInfo.builder()
                .id(favorite.getId())
                .name(favorite.getName())
                .isPublic(favorite.getIsPublic())
                .restaurantLists(restaurants)
                .build();
    }

    public void saveFavoriteList(User user, FavoriteDto.CreateFavoriteListRequest createFavoriteListRequest) {
        Favorite favorite = Favorite.builder()
                .name(createFavoriteListRequest.getName())
                .isPublic(createFavoriteListRequest.getIsPublic())
                .user(user)
                .build();

        favoriteRepository.save(favorite);
    }

    public FavoriteDto.FavoriteInfo getFavorite(Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(() -> new RuntimeException("찜 목록을 찾을 수 없습니다."));
        return toFavoriteInfo(favorite);
    }
}
