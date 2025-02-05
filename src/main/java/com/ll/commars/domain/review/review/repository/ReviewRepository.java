package com.ll.commars.domain.review.review.repository;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.review.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.restaurant = :restaurant")
    List<Review> findByRestaurant(Restaurant restaurant);
}
