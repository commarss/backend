package com.ll.commars.domain.favorite.favorite.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.favorite.favorite.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	List<Favorite> findByMemberId(Long memberId);
}
