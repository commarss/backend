package com.ll.commars.domain.favorite.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.favorite.dto.FavoriteCreateRequest;
import com.ll.commars.domain.favorite.dto.FavoriteCreateResponse;
import com.ll.commars.domain.favorite.dto.FavoriteFindListResponse;
import com.ll.commars.domain.favorite.dto.FavoriteRestaurantCreateRequest;
import com.ll.commars.domain.favorite.dto.FavoriteRestaurantCreateResponse;
import com.ll.commars.domain.favorite.dto.FavoriteRestaurantsResponse;
import com.ll.commars.domain.favorite.entity.Favorite;
import com.ll.commars.domain.favorite.entity.FavoriteRestaurant;
import com.ll.commars.domain.favorite.repository.jpa.FavoriteRepository;
import com.ll.commars.domain.favorite.repository.jpa.FavoriteRestaurantRepository;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.global.exception.CustomException;
import com.ll.commars.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final FavoriteRepository favoriteRepository;
	private final FavoriteRestaurantRepository favoriteRestaurantRepository;
	private final MemberRepository memberRepository;
	private final RestaurantRepository restaurantRepository;

	@Transactional
	public FavoriteCreateResponse createFavorite(FavoriteCreateRequest request, Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		Favorite favorite = new Favorite(request.name(), request.isPublic(), member);

		Favorite savedFavorite = favoriteRepository.save(favorite);

		return FavoriteCreateResponse.from(savedFavorite);
	}

	@Transactional(readOnly = true)
	public FavoriteFindListResponse getFavorites(Long memberId) {
		List<Favorite> favorites = favoriteRepository.findByMemberId(memberId);
		List<FavoriteFindListResponse.FavoriteFindResponse> favoriteResponses = favorites.stream()
			.map(favorite -> new FavoriteFindListResponse.FavoriteFindResponse(
				favorite.getId(),
				favorite.getName(),
				favorite.isPublic()))
			.toList();

		return new FavoriteFindListResponse(favoriteResponses);
	}

	@Transactional(readOnly = true)
	public FavoriteRestaurantsResponse getFavoriteRestaurants(Long favoriteId, Long memberId) {
		Favorite favorite = favoriteRepository.findById(favoriteId)
			.orElseThrow(() -> new CustomException(ErrorCode.FAVORITE_NOT_FOUND));

		validateFavoriteOwnership(favorite, memberId);

		List<FavoriteRestaurantsResponse.FavoriteRestaurantInfo> restaurantInfos = favorite.getFavoriteRestaurants()
			.stream()
			.map(favoriteRestaurant -> new FavoriteRestaurantsResponse.FavoriteRestaurantInfo(
				favoriteRestaurant.getRestaurant().getId(),
				favoriteRestaurant.getRestaurant().getName(),
				favoriteRestaurant.getRestaurant().getAddress(),
				favoriteRestaurant.getRestaurant().getRestaurantCategory().name(),
				favoriteRestaurant.getRestaurant().getAverageRate(),
				favoriteRestaurant.getRestaurant().getImageUrl()
			))
			.toList();

		return new FavoriteRestaurantsResponse(restaurantInfos, restaurantInfos.size());
	}

	@Transactional
	public void deleteFavorite(Long favoriteId, Long memberId) {
		Favorite favorite = favoriteRepository.findById(favoriteId)
			.orElseThrow(() -> new CustomException(ErrorCode.FAVORITE_NOT_FOUND));

		validateFavoriteOwnership(favorite, memberId);
		favoriteRepository.delete(favorite);
	}

	@Transactional
	public FavoriteRestaurantCreateResponse createFavoriteRestaurant(Long restaurantId, Long memberId,
		FavoriteRestaurantCreateRequest request) {
		Favorite favorite = favoriteRepository.findById(request.favoriteId())
			.orElseThrow(() -> new CustomException(ErrorCode.FAVORITE_NOT_FOUND));

		validateFavoriteOwnership(favorite, memberId);

		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new CustomException(ErrorCode.RESTAURANT_NOT_FOUND));

		validateFavoriteRestaurantExists(favorite, restaurant);

		FavoriteRestaurant favoriteRestaurant = new FavoriteRestaurant(favorite, restaurant);
		FavoriteRestaurant savedFavoriteRestaurant = favoriteRestaurantRepository.save(favoriteRestaurant);

		return FavoriteRestaurantCreateResponse.from(savedFavoriteRestaurant);
	}

	private void validateFavoriteRestaurantExists(Favorite favorite, Restaurant restaurant) {
		if (favoriteRestaurantRepository.existsByFavoriteAndRestaurant(favorite, restaurant)) {
			throw new CustomException(ErrorCode.FAVORITE_RESTAURANT_ALREADY_EXISTS);
		}
	}

	@Transactional
	public void deleteFavoriteRestaurant(Long restaurantId, Long memberId) {
		restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new CustomException(ErrorCode.RESTAURANT_NOT_FOUND));

		favoriteRestaurantRepository.deleteAllByRestaurantIdAndMemberId(restaurantId, memberId);
	}

	private void validateFavoriteOwnership(Favorite favorite, Long memberId) {
		if (!favorite.getMember().getId().equals(memberId)) {
			throw new CustomException(ErrorCode.FAVORITE_FORBIDDEN);
		}
	}
}
