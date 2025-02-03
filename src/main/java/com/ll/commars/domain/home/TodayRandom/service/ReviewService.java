package com.ll.commars.domain.home.TodayRandom.service;

import com.ll.commars.domain.home.TodayRandom.dto.ReviewerRank;
import com.ll.commars.domain.home.TodayRandom.repository.MypageRepository;
import com.ll.commars.domain.home.TodayRandom.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {

    private final RestaurantRepository restaurantRepository;
    private final MypageRepository mypageRepository;

    @Autowired
    public ReviewService(RestaurantRepository restaurantRepository, MypageRepository mypageRepository) {
        this.restaurantRepository = restaurantRepository;
        this.mypageRepository = mypageRepository;
    }

    public List<ReviewerRank> getTopReviewers() {
        // Step 1: restaurants 테이블에서 리뷰어별 리뷰 횟수
        List<Object[]> restaurantReviewers = restaurantRepository.findTopReviewers();

        // Step 2: mypage 테이블에서 리뷰어별 리뷰 횟수
        List<Object[]> mypageReviewers = mypageRepository.findTopReviewersFromMypage();

        // Step 3: 리뷰어별 카운트를 담을 맵
        Map<String, Integer> reviewerCounts = new HashMap<>();

        // Step 4: 콤마로 구분된 리뷰어를 분리하여 카운트하기
        countReviewers(restaurantReviewers, reviewerCounts);
        countReviewers(mypageReviewers, reviewerCounts);

        // Step 5: 리뷰어를 카운트 내림차순으로 정렬
        List<Map.Entry<String, Integer>> sortedReviewers = new ArrayList<>(reviewerCounts.entrySet());
        sortedReviewers.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Step 6: TOP 10 리뷰어와 등장 횟수 반환
        List<ReviewerRank> topReviewers = new ArrayList<>();
        for (int i = 0; i < Math.min(10, sortedReviewers.size()); i++) {
            Map.Entry<String, Integer> entry = sortedReviewers.get(i);
            topReviewers.add(new ReviewerRank(entry.getKey(), entry.getValue()));
        }

        return topReviewers;
    }

    // 리뷰어들을 분리해서 카운트하는 메소드
    private void countReviewers(List<Object[]> reviewerData, Map<String, Integer> reviewerCounts) {
        for (Object[] result : reviewerData) {
            String reviewers = (String) result[0];
            // 리뷰어들이 콤마로 구분되어 있으므로 이를 분리
            String[] separatedReviewers = reviewers.split(",");
            for (String reviewer : separatedReviewers) {
                reviewerCounts.put(reviewer, reviewerCounts.getOrDefault(reviewer, 0) + 1);
            }
        }
    }


}


/*

레스토랑테이블과 찜테이블에서 콤마를 기준으로 1개의 리뷰어의 총 횟수 순위로 출력
http://localhost:8080/api/reviewers/top10
[
    {
        "reviewer": "리뷰어3",
        "count": 4
    },
    {
        "reviewer": "리뷰어2",
        "count": 4
    },
    {
        "reviewer": "리뷰어1",
        "count": 4
    },
    {
        "reviewer": "리뷰어6",
        "count": 3
    },
    {
        "reviewer": "리뷰어5",
        "count": 3
    },
    {
        "reviewer": "리뷰어4",
        "count": 3
    }
]
 */