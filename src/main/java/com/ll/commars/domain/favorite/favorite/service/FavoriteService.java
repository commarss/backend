package com.ll.commars.domain.favorite.favorite.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.favorite.favorite.dto.FavoriteDto;
import com.ll.commars.domain.favorite.favorite.entity.Favorite;
import com.ll.commars.domain.favorite.favorite.repository.jpa.FavoriteRepository;
import com.ll.commars.domain.favorite.favorite.repository.jpa.FavoriteRestaurantRepository;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final FavoriteRepository favoriteRepository;
	private final RestaurantRepository restaurantRepository;
	private final FavoriteRestaurantRepository favoriteRestaurantRepository;

	public int getFavoriteCount(String email) {
		return favoriteRepository.countByUserEmail(email);
	}

	public List<Favorite> getFavoritesByUser(Member member) {
		return favoriteRepository.findByMemberEmail(member.getEmail());
	}

	// public FavoriteDto.FavoriteInfo toFavoriteInfo(Favorite favorite) {
	// 	List<RestaurantDto.RestaurantBasicInfo> restaurants = Optional.ofNullable(favorite.getFavoriteRestaurants())
	// 		.orElse(Collections.emptyList()) // Null이면 빈 리스트 반환
	// 		.stream()
	// 		.map(fr -> RestaurantDto.RestaurantBasicInfo.builder()
	// 			.id(fr.getRestaurant().getId())
	// 			.name(fr.getRestaurant().getName())
	// 			.details(fr.getRestaurant().getDetails())
	// 			.averageRate(fr.getRestaurant().getAverageRate())
	// 			.imageUrl(fr.getRestaurant().getImageUrl())
	// 			.contact(fr.getRestaurant().getContact())
	// 			.lat(fr.getRestaurant().getLat())
	// 			.lon(fr.getRestaurant().getLon())
	// 			.runningState(fr.getRestaurant().getRunningState())
	// 			.summarizedReview(fr.getRestaurant().getSummarizedReview())
	// 			.categoryId(fr.getRestaurant().getRestaurantCategory().getId())
	// 			.address(fr.getRestaurant().getAddress())
	// 			.build())
	// 		.collect(Collectors.toList());
	//
	// 	return FavoriteDto.FavoriteInfo.builder()
	// 		.id(favorite.getId())
	// 		.name(favorite.getName())
	// 		.isPublic(favorite.getIsPublic() != null ? favorite.getIsPublic() : true)
	// 		.restaurantLists(restaurants)
	// 		.build();
	// }

	public void saveFavoriteList(Member member, FavoriteDto.CreateFavoriteListRequest createFavoriteListRequest) {
		Boolean isPublicValue = createFavoriteListRequest.getIsPublic();
		Favorite favorite = Favorite.builder()
			.name(createFavoriteListRequest.getName())
			.isPublic(isPublicValue != null ? isPublicValue : true)
			.member(member)
			.build();

		favoriteRepository.save(favorite);
	}

	// @Transactional
	// public FavoriteDto.FavoriteInfo getFavorite(Long favoriteId) {
	// 	Favorite favorite = favoriteRepository.findById(favoriteId)
	// 		.orElseThrow(() -> new RuntimeException("찜 목록을 찾을 수 없습니다."));
	// 	return toFavoriteInfo(favorite);
	// }

	// @Transactional
	// public FavoriteDto.FavoriteInfo addRestaurantToFavorite(Long favoriteId, Long restaurantId) {
	// 	// 찜 목록 조회
	// 	Favorite favorite = favoriteRepository.findById(favoriteId)
	// 		.orElseThrow(() -> new IllegalArgumentException("Favorite not found"));
	//
	// 	// 레스토랑 조회
	// 	Restaurant restaurant = restaurantRepository.findById(restaurantId)
	// 		.orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
	//
	// 	// 이미 추가된 레스토랑인지 확인
	// 	boolean isExists = favorite.getFavoriteRestaurants().stream()
	// 		.anyMatch(fr -> fr.getRestaurant().getId().equals(restaurantId));
	//
	// 	if (isExists) {
	// 		throw new IllegalStateException("Restaurant already exists in favorite list");
	// 	}
	//
	// 	// FavoriteRestaurant 생성 및 저장
	// 	FavoriteRestaurant favoriteRestaurant = FavoriteRestaurant.builder()
	// 		.favorite(favorite)
	// 		.restaurant(restaurant)
	// 		.build();
	//
	// 	favoriteRestaurantRepository.save(favoriteRestaurant);
	//
	// 	// 업데이트된 찜 목록 정보 반환
	// 	return toFavoriteInfo(favorite);
	// }

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
	public Optional<Favorite> isFavorite(Member member, Long restaurantId) {
		return favoriteRepository.findByMemberAndFavoriteRestaurantsRestaurantId(member, restaurantId);
	}

	public Favorite saveFavorite(Favorite favorite) {
		return favoriteRepository.save(favorite);
	}

	public Optional<Favorite> findByUserAndName(Member member, String name) {
		return favoriteRepository.findByMemberAndName(member, name);
	}

	// @Transactional
	// public List<FavoriteDto.FavoriteInfo> getAllFavoritesByUser(Long memberId) {
	// 	List<Favorite> favorites = favoriteRepository.findByMemberId(memberId);  // 사용자의 찜 목록 조회
	// 	return favorites.stream()
	// 		.map(favorite -> toFavoriteInfo(favorite))  // Favorite 객체를 FavoriteInfo로 변환
	// 		.collect(Collectors.toList());
	// }

}
