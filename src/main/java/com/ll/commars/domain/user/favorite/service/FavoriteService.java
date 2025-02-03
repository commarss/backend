package com.ll.commars.domain.user.favorite.service;

import com.ll.commars.domain.user.favorite.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    public int getFavoriteCount(String email) {
        return favoriteRepository.countByUserEmail(email);
    }

    // 찜 목록 조회

    // 찜 목록 추가
}
