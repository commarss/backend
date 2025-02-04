package com.ll.commars.domain.user.favorite.repository;

import com.ll.commars.domain.user.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // 특정 리뷰어(email)의 찜 목록을 조회
    List<Favorite> findByUserEmail(String email);

    // 특정 리뷰어와 식당에 대한 찜이 이미 존재하는지 체크
    Optional<Favorite> findByUserEmailAndId(String email, Long restaurantId);

    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.user.email = :email")
    int countByUserEmail(@Param("email") String email);
}
