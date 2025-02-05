package com.ll.commars.domain.user.favorite.controller;

import com.ll.commars.domain.user.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorite")
public class ApiV1FavoriteController {
    private final FavoriteService favoriteService;

    // 찜 목록 개수 조회
    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getFavoriteCount(@RequestParam String email) {
        int count = favoriteService.getFavoriteCount(email);
        Map<String, Integer> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    // 찜 목록 조회

    // 찜 목록 추가
}
