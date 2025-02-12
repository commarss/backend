package com.ll.commars.domain.user.favorite.service;

import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantDto;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.RestaurantRepository;
import com.ll.commars.domain.user.favorite.dto.FavoriteDto;
import com.ll.commars.domain.user.favorite.entity.Favorite;
import com.ll.commars.domain.user.favorite.repository.FavoriteRepository;
import com.ll.commars.domain.user.favoriteRestaurant.entity.FavoriteRestaurant;
import com.ll.commars.domain.user.favoriteRestaurant.repository.FavoriteRestaurantRepository;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final RestaurantRepository restaurantRepository;
    private final FavoriteRestaurantRepository favoriteRestaurantRepository;
    public int getFavoriteCount(String email) {
        return favoriteRepository.countByUserEmail(email);
    }

    public List<Favorite> getFavoritesByUser(User user) {
        return favoriteRepository.findByUserEmail(user.getEmail());
    }

    public FavoriteDto.FavoriteInfo toFavoriteInfo(Favorite favorite) {
        List<RestaurantDto.RestaurantBasicInfo> restaurants = Optional.ofNullable(favorite.getFavoriteRestaurants())
                .orElse(Collections.emptyList()) // Null이면 빈 리스트 반환
                .stream()
                .map(fr -> RestaurantDto.RestaurantBasicInfo.builder()
                        .id(fr.getRestaurant().getId())
                        .name(fr.getRestaurant().getName())
                        .details(fr.getRestaurant().getDetails())
                        .averageRate(fr.getRestaurant().getAverageRate())
                        .imageUrl(fr.getRestaurant().getImageUrl())
                        .contact(fr.getRestaurant().getContact())
                        .lat(fr.getRestaurant().getLat())
                        .lng(fr.getRestaurant().getLng())
                        .runningState(fr.getRestaurant().getRunningState())
                        .summarizedReview(fr.getRestaurant().getSummarizedReview())
                        .categoryId(fr.getRestaurant().getRestaurantCategory().getId())
                        .address(fr.getRestaurant().getAddress())
                        .build())
                .collect(Collectors.toList());

        return FavoriteDto.FavoriteInfo.builder()
                .id(favorite.getId())
                .name(favorite.getName())
                .isPublic(favorite.getIsPublic() != null ? favorite.getIsPublic() : true)
                .restaurantLists(restaurants)
                .build();
    }

    public void saveFavoriteList(User user, FavoriteDto.CreateFavoriteListRequest createFavoriteListRequest) {
        Boolean isPublicValue = createFavoriteListRequest.getIsPublic();
        System.out.println("📌 [디버깅] 받은 isPublic 값: " + isPublicValue);
        Favorite favorite = Favorite.builder()
                .name(createFavoriteListRequest.getName())
                .isPublic(isPublicValue != null ? isPublicValue : true)
                .user(user)
                .build();

        favoriteRepository.save(favorite);
        System.out.println("📌 [디버깅] 저장된 isPublic 값: " + favorite.getIsPublic());
    }

    @Transactional
    public FavoriteDto.FavoriteInfo getFavorite(Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(() -> new RuntimeException("찜 목록을 찾을 수 없습니다."));
        return toFavoriteInfo(favorite);
    }

    @Transactional
    public FavoriteDto.FavoriteInfo addRestaurantToFavorite(Long favoriteId, Long restaurantId) {
        // 찜 목록 조회
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new IllegalArgumentException("Favorite not found"));

        // 레스토랑 조회
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        // 이미 추가된 레스토랑인지 확인
        boolean isExists = favorite.getFavoriteRestaurants().stream()
                .anyMatch(fr -> fr.getRestaurant().getId().equals(restaurantId));

        if (isExists) {
            throw new IllegalStateException("Restaurant already exists in favorite list");
        }

        // FavoriteRestaurant 생성 및 저장
        FavoriteRestaurant favoriteRestaurant = FavoriteRestaurant.builder()
                .favorite(favorite)
                .restaurant(restaurant)
                .build();

        favoriteRestaurantRepository.save(favoriteRestaurant);

        // 업데이트된 찜 목록 정보 반환
        return toFavoriteInfo(favorite);
    }

    @Transactional
    public void deleteFavorite(Long favoriteId) {
        favoriteRestaurantRepository.deleteByFavoriteId(favoriteId);
        favoriteRepository.deleteById(favoriteId);
    }

    @Transactional
    public void truncate() {
        favoriteRepository.deleteAll();
    }

    @Transactional
    public boolean isFavorite(User user, Long restaurantId) {
        return favoriteRepository.existsByUserAndFavoriteRestaurantsRestaurantId(user, restaurantId);
    }

    public Favorite saveFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    @Transactional
    public List<FavoriteDto.FavoriteInfo> getAllFavoritesByUser(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);  // 사용자의 찜 목록 조회
        return favorites.stream()
                .map(favorite -> toFavoriteInfo(favorite))  // Favorite 객체를 FavoriteInfo로 변환
                .collect(Collectors.toList());
    }

}
