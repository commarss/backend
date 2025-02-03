package com.ll.commars.domain.home.TodayRandom.service;

import com.ll.commars.domain.home.TodayRandom.entity.MyFavorite;
import com.ll.commars.domain.home.TodayRandom.entity.Mypage;
import com.ll.commars.domain.home.TodayRandom.entity.Restaurant;
import com.ll.commars.domain.home.TodayRandom.repository.MyFavoriteRepository;
import com.ll.commars.domain.home.TodayRandom.repository.MypageRepository;
import com.ll.commars.domain.home.TodayRandom.repository.RestaurantRepository;

import com.ll.commars.domain.home.board.entity.User;
import com.ll.commars.domain.home.board.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MypageRepository mypageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyFavoriteRepository myFavoriteRepository;

    public List<Restaurant> getNearbyRestaurants(double lat, double lng, double radius) {
        List<Restaurant> results = restaurantRepository.findNearby(lat, lng, radius);
        System.out.println("쿼리 결과 (Nearby): " + results);
        return results;
    }

    public Restaurant getRestaurantDetails(String name) {
        return restaurantRepository.findByName(name);
    }

    public List<String> getRandomRestaurants(double lat, double lng, String email) {
        try {
            // 반경 2km 내 식당 검색
            List<Restaurant> nearbyRestaurants = restaurantRepository.findNearby(lat, lng, 2.0);
            System.out.println("Nearby Restaurants: " + nearbyRestaurants);

            // 찜한 식당
            List<Restaurant> favoriteRestaurants = mypageRepository.findByUserEmail(email)
                    .stream()
                    .map(Mypage::getRestaurant)
                    .collect(Collectors.toList());
            System.out.println("Favorite Restaurants: " + favoriteRestaurants);

            // 겹치는 키워드를 가진 식당
            Set<String> userKeywords = mypageRepository.findByUserEmail(email).stream()
                    .map(Mypage::getKeywords)
                    .flatMap(keywords -> Arrays.stream(keywords.split(",")))
                    .collect(Collectors.toSet());
            System.out.println("User Keywords: " + userKeywords);

            List<Restaurant> keywordMatchingRestaurants = nearbyRestaurants.stream()
                    .filter(restaurant -> {
                        Set<String> restaurantKeywords = Arrays.stream(restaurant.getKeywords().split(","))
                                .collect(Collectors.toSet());
                        restaurantKeywords.retainAll(userKeywords);
                        return !restaurantKeywords.isEmpty();
                    })
                    .collect(Collectors.toList());
            System.out.println("Keyword Matching Restaurants: " + keywordMatchingRestaurants);

            // 중복 제거 후 랜덤 5개 반환
            Set<Restaurant> combinedSet = new HashSet<>();
            combinedSet.addAll(nearbyRestaurants);
            combinedSet.addAll(favoriteRestaurants);
            combinedSet.addAll(keywordMatchingRestaurants);

            List<Restaurant> combinedList = new ArrayList<>(combinedSet);
            Collections.shuffle(combinedList);

            return combinedList.stream()
                    .map(Restaurant::getName)
                    .limit(5)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace(); // 디버깅을 위한 에러 출력
            throw new RuntimeException("Error while fetching random restaurants", e);
        }
    }

    public List<String> getTop10Keywords() {
        return restaurantRepository.findTop10Keywords();
    }

    public List<Restaurant> searchByKeyword(String keyword) {
        List<Restaurant> results = restaurantRepository.searchByKeyword(keyword);
        System.out.println("쿼리 결과 (Search): " + results);
        return results;
    }


    public void saveMypage(String email, Long restaurantId, String reviewer, List<String> keywords, String review, double score) {
        // ✅ `reviewer` 값으로 `User` 찾기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        // ✅ `restaurantId` 값으로 `Restaurant` 찾기
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found: " + restaurantId));

        Mypage mypage = new Mypage();
        mypage.setUser(user);  // ✅ `user` 필드 설정
        mypage.setRestaurant(restaurant);
        mypage.setReviewer(reviewer);
        mypage.setKeywords(String.join(",", keywords)); // 리스트를 문자열로 변환
        mypage.setReview(review);
        mypage.setScore(score);

        mypageRepository.save(mypage);
    }


    // 찜 목록에 추가
    public boolean saveToFavorites(String email, Long restaurantId) {
        Optional<MyFavorite> existingFavorite = myFavoriteRepository.findByUserEmailAndRestaurantId(email, restaurantId);
        if (existingFavorite.isPresent()) {
            return false;  // 이미 찜 목록에 존재하는 경우
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MyFavorite favorite = new MyFavorite();
        favorite.setUser(user);  // User를 외래키로 설정
        favorite.setRestaurant(restaurant);  // Restaurant를 설정

        myFavoriteRepository.save(favorite);
        return true;  // 찜 목록에 추가된 경우
    }

    // 찜 목록 조회
    public List<Restaurant> getFavoriteRestaurants(String email) {
        List<MyFavorite> favorites = myFavoriteRepository.findByUserEmail(email);
        return favorites.stream()
                .map(MyFavorite::getRestaurant)  // MyFavorite에서 Restaurant 객체 반환
                .collect(Collectors.toList());
    }



    public int getFavoriteCount(String email) {
        return myFavoriteRepository.countByUserEmail(email);
    }
}
