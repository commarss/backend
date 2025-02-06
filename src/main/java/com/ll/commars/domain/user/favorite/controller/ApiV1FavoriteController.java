package com.ll.commars.domain.user.favorite.controller;

import com.ll.commars.domain.user.favorite.dto.FavoriteDto;
import com.ll.commars.domain.user.favorite.service.FavoriteService;
import com.ll.commars.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/favorite", produces = APPLICATION_JSON_VALUE)
@Tag(name = "ApiV1FavoriteController", description = "찜 목록 API")
public class ApiV1FavoriteController {
    private final FavoriteService favoriteService;

    // 찜 목록 개수 조회
    @GetMapping("/count")
    @Operation(summary = "찜 목록 개수 조회")
    public ResponseEntity<Map<String, Integer>> getFavoriteCount(@RequestParam String email) {
        int count = favoriteService.getFavoriteCount(email);
        Map<String, Integer> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    // 특정 찜 목록 조회
    @GetMapping("/{favorite_id}")
    @Operation(summary = "찜 목록에 존재하는 식당 조회")
    public RsData<FavoriteDto.FavoriteInfo> getFavorite(@PathVariable Long favoriteId) {
        FavoriteDto.FavoriteInfo response = favoriteService.getFavorite(favoriteId);
        return new RsData<>("200", "찜 목록 조회 성공", response);
    }

    // 찜 목록 추가
}
