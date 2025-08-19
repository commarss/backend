package com.ll.commars.domain.todayRandom.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ll.commars.domain.favorite.favorite.repository.jpa.FavoriteRepository;
import com.ll.commars.domain.favorite.favorite.repository.jpa.FavoriteRestaurantRepository;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.domain.review.dto.RestaurantReviewAnalysisDTO;
import com.ll.commars.domain.review.dto.ReviewAnalysisDTO;
import com.ll.commars.domain.review.entity.Review;
import com.ll.commars.domain.review.repository.jpa.ReviewRepository;

@Service

public class TodayRandomService {

	private final RestaurantRepository restaurantRepository;
	private final FavoriteRestaurantRepository favoriteRestaurantRepository;
	private final FavoriteRepository favoriteRepository;
	private final ReviewRepository reviewRepository;

	@Autowired
	public TodayRandomService(RestaurantRepository restaurantRepository,
		FavoriteRestaurantRepository favoriteRestaurantRepository,
		FavoriteRepository favoriteRepository,
		ReviewRepository reviewRepository) {
		this.restaurantRepository = restaurantRepository;
		this.favoriteRestaurantRepository = favoriteRestaurantRepository;
		this.favoriteRepository = favoriteRepository;
		this.reviewRepository = reviewRepository;
	}

	// @Transactional(readOnly = true) // Lazy Loading 방지
	// public List<RestaurantSummaryDTO> getRandomRestaurants(double lat, double lon, Long memberId) {
	// 	// 1. 유저의 찜 목록 가져오기
	// 	List<Favorite> favorites = favoriteRepository.findByMemberId(memberId);
	// 	Set<Long> favoriteIds = favorites.stream()
	// 		.map(Favorite::getId)
	// 		.collect(Collectors.toSet());
	//
	// 	// 2. 찜 목록에 해당하는 식당 가져오기
	// 	List<FavoriteRestaurant> favoriteRestaurants = favoriteRestaurantRepository.findByFavoriteIdIn(favoriteIds);
	// 	Set<Long> favoriteRestaurantIds = favoriteRestaurants.stream()
	// 		.map(fr -> fr.getRestaurant().getId())
	// 		.collect(Collectors.toSet());
	//
	// 	// 3. 거리 내의 식당 목록 가져오기
	// 	List<Restaurant> nearbyRestaurants = restaurantRepository.findRestaurantsWithinRadius(lat, lon);
	//
	// 	// 4. 반경 내 찜한 식당 필터링
	// 	List<Restaurant> filteredRestaurants = nearbyRestaurants.stream()
	// 		.filter(r -> favoriteRestaurantIds.contains(r.getId()))
	// 		.collect(Collectors.toList());
	//
	// 	// 5. 랜덤하게 5개 선택 후 DTO 변환
	// 	Collections.shuffle(filteredRestaurants);
	// 	return filteredRestaurants.stream()
	// 		.limit(5)
	// 		.map(RestaurantSummaryDTO::fromEntity)
	// 		.collect(Collectors.toList());
	// }

    /*@Transactional(readOnly = true) //랜덤으로 1개 선택
    public Optional<RestaurantSummaryDTO> getRandomRestaurantDetails(double lat, double lon, Long memberId) {
        List<RestaurantSummaryDTO> randomRestaurants = getRandomRestaurants(lat, lon, memberId);

        if (randomRestaurants.isEmpty()) {
            return Optional.empty();
        }

        // 랜덤으로 1개 선택
        return Optional.of(randomRestaurants.get(new Random().nextInt(randomRestaurants.size())));
    }*/

	// @Transactional(readOnly = true)
	// public Optional<RestaurantSummaryDTO> getRestaurantDetails(Long restaurantId) {
	// 	return restaurantRepository.findById(restaurantId)
	// 		.map(RestaurantSummaryDTO::fromEntity);
	// }
	//
	// @Transactional(readOnly = true)
	// public List<RestaurantSummaryDTO> getnotuserRandomRestaurants(double lat, double lon) {
	// 	// 반경 2km 내의 식당 목록 가져오기
	// 	List<Restaurant> nearbyRestaurants = restaurantRepository.findRestaurantsWithinRadius(lat, lon);
	//
	// 	// 식당이 5개 이하라면 그대로 반환
	// 	if (nearbyRestaurants.size() <= 5) {
	// 		return nearbyRestaurants.stream()
	// 			.map(RestaurantSummaryDTO::fromEntity)
	// 			.collect(Collectors.toList());
	// 	}
	//
	// 	// 무작위로 5개 선택
	// 	Collections.shuffle(nearbyRestaurants);
	// 	return nearbyRestaurants.stream()
	// 		.limit(5)
	// 		.map(RestaurantSummaryDTO::fromEntity)
	// 		.collect(Collectors.toList());
	// }

	// 리뷰 분석 (레스토랑 이름, 리뷰 개수, 평균 평점, 가중치 점수)

	// 특정 레스토랑 리뷰 분석
	public RestaurantReviewAnalysisDTO getRestaurantReviewAnalysis(Long restaurantId) {
		// 레스토랑 정보 가져오기
		Optional<Restaurant> restaurantOpt = restaurantRepository.findById(restaurantId);

		if (restaurantOpt.isEmpty()) {
			return null;  // 레스토랑이 없을 경우 예외 처리
		}

		Restaurant restaurant = restaurantOpt.get();
		String restaurantName = restaurant.getName();

		// 해당 레스토랑의 리뷰 정보 가져오기
		List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);
		int reviewCount = reviews.size();
		double averageRating = reviews.stream()
			.mapToInt(Review::getRate)
			.average()
			.orElse(0.0);

		// 가중치 점수 계산: 평점 + (리뷰 개수 * 0.3)
		double weightedScore = averageRating + (reviewCount * 0.3);

		// DTO 반환 (순위는 단일 조회 시 의미 없음)

		// 리뷰 정보 DTO 변환
		List<ReviewAnalysisDTO> reviewDTOList = reviews.stream()
			.map(review -> new ReviewAnalysisDTO(review.getTitle(), review.getBody(), review.getRate()))
			.collect(Collectors.toList());

		return new RestaurantReviewAnalysisDTO(
			restaurantId,
			restaurantName,
			reviewCount,
			averageRating,
			weightedScore,
			0, // 순위는 나중에 설정
			reviewDTOList
		);
	}

	// 전체 레스토랑 분석 (순위 상위 20개만 반환)
	public List<RestaurantReviewAnalysisDTO> getAllRestaurantReviewAnalysis() {
		// 모든 레스토랑 가져오기
		List<Restaurant> restaurants = restaurantRepository.findAll();

		// 각 레스토랑에 대한 리뷰 분석 점수 계산
		List<RestaurantReviewAnalysisDTO> analysisDTOList = restaurants.stream()
			.map(restaurant -> {
				Long restaurantId = restaurant.getId();
				String restaurantName = restaurant.getName();

				// 해당 레스토랑의 리뷰 정보 가져오기
				List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);
				int reviewCount = reviews.size();
				double averageRating = reviews.stream()
					.mapToInt(Review::getRate)
					.average()
					.orElse(0.0);

				// 가중치 점수 계산: 평점 + (리뷰 개수 * 0.3)
				double weightedScore = averageRating + (reviewCount * 0.3);

				// DTO 반환 (순위는 나중에 설정)
				// 리뷰 정보 DTO 변환
				List<ReviewAnalysisDTO> reviewDTOList = reviews.stream()
					.map(review -> new ReviewAnalysisDTO(review.getTitle(), review.getBody(), review.getRate()))
					.collect(Collectors.toList());

				return new RestaurantReviewAnalysisDTO(
					restaurantId,
					restaurantName,
					reviewCount,
					averageRating,
					weightedScore,
					0, // 순위는 나중에 계산
					reviewDTOList
				);
			})
			.collect(Collectors.toList());

		// 가중치 점수를 기준으로 내림차순 정렬 (높은 순서대로)
		analysisDTOList.sort((a, b) -> Double.compare(b.getWeightedScore(), a.getWeightedScore()));

		// 순위 부여 (최대 20위까지만)
		for (int i = 0; i < analysisDTOList.size() && i < 20; i++) {
			analysisDTOList.get(i).setRank(i + 1);
		}

		// 상위 20개만 반환
		return analysisDTOList.stream().limit(20).collect(Collectors.toList());
	}
}
