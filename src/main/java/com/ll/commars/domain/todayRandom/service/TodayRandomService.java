package com.ll.commars.domain.todayRandom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TodayRandomService {

//    @Autowired
//    private RestaurantRepository restaurantRepository;
//
//    @Autowired
//    private MypageRepository mypageRepository;

//    public List<String> getRandomRestaurants(double lat, double lng, String email) {
//        try {
//            // 반경 2km 내 식당 검색
//            List<Restaurant> nearbyRestaurants = restaurantRepository.findNearby(lat, lng, 2.0);
//            System.out.println("Nearby Restaurants: " + nearbyRestaurants);
//
//            // 찜한 식당
//            List<Restaurant> favoriteRestaurants = mypageRepository.findByUserEmail(email)
//                    .stream()
//                    .map(Mypage::getRestaurant)
//                    .collect(Collectors.toList());
//            System.out.println("Favorite Restaurants: " + favoriteRestaurants);
//
//            // 겹치는 키워드를 가진 식당
//            Set<String> userKeywords = mypageRepository.findByUserEmail(email).stream()
//                    .map(Mypage::getKeywords)
//                    .flatMap(keywords -> Arrays.stream(keywords.split(",")))
//                    .collect(Collectors.toSet());
//            System.out.println("User Keywords: " + userKeywords);
//
//            List<Restaurant> keywordMatchingRestaurants = nearbyRestaurants.stream()
//                    .filter(restaurant -> {
//                        Set<String> restaurantKeywords = Arrays.stream(restaurant.getKeywords().split(","))
//                                .collect(Collectors.toSet());
//                        restaurantKeywords.retainAll(userKeywords);
//                        return !restaurantKeywords.isEmpty();
//                    })
//                    .collect(Collectors.toList());
//            System.out.println("Keyword Matching Restaurants: " + keywordMatchingRestaurants);
//
//            // 중복 제거 후 랜덤 5개 반환
//            Set<Restaurant> combinedSet = new HashSet<>();
//            combinedSet.addAll(nearbyRestaurants);
//            combinedSet.addAll(favoriteRestaurants);
//            combinedSet.addAll(keywordMatchingRestaurants);
//
//            List<Restaurant> combinedList = new ArrayList<>(combinedSet);
//            Collections.shuffle(combinedList);
//
//            return combinedList.stream()
//                    .map(Restaurant::getName)
//                    .limit(5)
//                    .collect(Collectors.toList());
//        } catch (Exception e) {
//            e.printStackTrace(); // 디버깅을 위한 에러 출력
//            throw new RuntimeException("Error while fetching random restaurants", e);
//        }
//    }
}
