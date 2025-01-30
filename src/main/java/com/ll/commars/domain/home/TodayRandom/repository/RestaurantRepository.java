package com.ll.commars.domain.home.TodayRandom.repository;

import com.ll.commars.domain.home.TodayRandom.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    // Custom Query: 리뷰어별 리뷰 횟수 계산
    @Query("SELECT r.reviewer, COUNT(r.reviewer) AS reviewerCount FROM Restaurant r " +
            "GROUP BY r.reviewer ORDER BY reviewerCount DESC")
    List<Object[]> findTopReviewers();

    // 반경 내의 식당 검색
    @Query("SELECT r FROM Restaurant r WHERE " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(r.latitude)) * " +
            "cos(radians(r.longitude) - radians(:lng)) + sin(radians(:lat)) * sin(radians(r.latitude)))) <= :radius")
    List<Restaurant> findNearby(@Param("lat") double lat,
                                @Param("lng") double lng,
                                @Param("radius") double radius);

    Restaurant findByName(String name);
}
/*
@Query(value = "SELECT * FROM restaurants WHERE ST_Distance_Sphere(Point(longitude, latitude), Point(:lng, :lat)) <= :radius", nativeQuery = true)
    List<Restaurant> findNearby(@Param("lat") double lat, @Param("lng") double lng, @Param("radius") double radius);


    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(r.keywords) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Restaurant> searchByKeyword(@Param("keyword") String keyword);


    Restaurant findByName(String name);
    @Query("SELECT r FROM Restaurant r WHERE r.keywords LIKE %:keyword% ORDER BY r.rating DESC")
    List<Restaurant> findByKeywordsContaining(@Param("keyword") String keyword);

 */
