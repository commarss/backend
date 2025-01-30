package com.ll.commars.domain.home.TodayRandom.repository;


import com.ll.commars.domain.home.TodayRandom.entity.Mypage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MypageRepository extends JpaRepository<Mypage, Long> {
    // Custom Query: mypage에서 리뷰어별 리뷰 횟수 계산
    @Query("SELECT m.reviewer, COUNT(m.reviewer) AS reviewerCount FROM Mypage m " +
            "GROUP BY m.reviewer ORDER BY reviewerCount DESC")
    List<Object[]> findTopReviewersFromMypage();

    List<Mypage> findByUserEmail(String email);
}



/*
@Query("SELECT DISTINCT m.restaurant FROM Mypage m WHERE m.reviewer = :reviewer")
    Optional<Mypage> findByReviewerAndRestaurantId();
 */