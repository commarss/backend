package com.ll.commars.domain.user.favorite.controller;

import com.ll.commars.domain.user.favorite.dto.FavoriteDto;
import com.ll.commars.domain.user.favorite.entity.Favorite;
import com.ll.commars.domain.user.favorite.service.FavoriteService;
import com.ll.commars.domain.user.favoriteRestaurant.service.FavoriteRestaurantService;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.service.UserService;
import com.ll.commars.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/favorite", produces = APPLICATION_JSON_VALUE)
@Tag(name = "ApiV1FavoriteController", description = "찜 목록 API")
public class ApiV1FavoriteController {
    private final FavoriteService favoriteService;
    private final UserService userService;
    private final FavoriteRestaurantService favoriteRestaurantService;

    // 찜 목록 개수 조회
    @GetMapping("/count")
    @Operation(summary = "찜 목록 개수 조회")
    public ResponseEntity<Map<String, Integer>> getFavoriteCount(@RequestParam String email) {
        int count = favoriteService.getFavoriteCount(email);
        Map<String, Integer> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{favorite_id}")
    @Operation(summary = "찜 목록에 존재하는 식당 조회")
    public RsData<FavoriteDto.FavoriteInfo> getFavorite(@PathVariable("favorite_id") Long favoriteId) {
        FavoriteDto.FavoriteInfo response = favoriteService.getFavorite(favoriteId);
        return new RsData<>("200", "찜 목록 조회 성공", response);
    }

    @DeleteMapping("/{favorite_id}")
    @Operation(summary = "찜 목록 삭제")
    public RsData<String> deleteFavorite(@PathVariable("favorite_id") Long favoriteId) {
        favoriteService.deleteFavorite(favoriteId);
        return new RsData<>("200", "찜 목록 삭제 성공", "찜 목록이 삭제되었습니다.");
    }

    @PostMapping("/{favorite_id}/restaurant")
    @Operation(summary = "찜 목록에 식당 추가")
    public RsData<FavoriteDto.FavoriteInfo> addRestaurantToFavorite(
            @PathVariable("favorite_id") Long favoriteId,
            @RequestBody FavoriteDto.AddRestaurantRequest request
    ) {
        FavoriteDto.FavoriteInfo response = favoriteService.addRestaurantToFavorite(favoriteId, request.getRestaurantId());
        return new RsData<>("201", "식당 추가 성공", response);
    }

    @PostMapping("/restaurant/add")
    @Operation(summary = "찜 목록에 식당 추가")
    public ResponseEntity<?> addRestaurantToFavoriteList(
            @RequestBody Map<String, Object> request
            ) {
        String favoriteId = String.valueOf(request.get("favoriteId"));
        String restaurantId = String.valueOf(request.get("restaurantId"));
        FavoriteDto.FavoriteInfo response = favoriteService.addRestaurantToFavorite(Long.parseLong(favoriteId), Long.parseLong(restaurantId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @Operation(summary = "찜 목록 생성")
    public ResponseEntity<?> createFavoriteList(
            @RequestBody Map<String, Object> request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        System.out.println("request = " + request);
        String userId = userDetails.getUsername();
        return userService.createFavoriteList(String.valueOf(request.get("name")), userId);
    }

    @GetMapping("/check")
    @Operation(summary = "찜 목록에 식당이 존재하는지 확인")
    public ResponseEntity<?> checkFavorite(
            @RequestParam("restaurantId") Long restaurantId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.findById(Long.parseLong(userDetails.getUsername()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Optional<Favorite> isFavorite = favoriteService.isFavorite(user, restaurantId);
        Map<String, Long> response = new HashMap<>();

        if (isFavorite.isEmpty()) {
            response.put("isFavorite", null);
            return ResponseEntity.ok(response);
        }
        response.put("isFavorite", isFavorite.get().getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "사용자가 가진 모든 찜 목록 조회")
    public RsData<List<FavoriteDto.FavoriteInfo>> getFavorites(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.valueOf(userDetails.getUsername()); // 현재 로그인한 사용자 정보 가져오기
        List<FavoriteDto.FavoriteInfo> favoriteList = favoriteService.getAllFavoritesByUser(userId);
        return new RsData<>("200", "찜 목록 조회 성공", favoriteList);
    }

    @PostMapping("/delete/restaurant")
    @Operation(summary = "찜 목록에서 식당 삭제")
    public ResponseEntity<?> deleteRestaurantFromFavorite(
            @RequestBody Map<String, Object> request
    ) {
        String favoriteId = String.valueOf(request.get("favoriteId"));
        String restaurantId = String.valueOf(request.get("restaurantId"));
        favoriteRestaurantService.deleteRestaurantFromFavorite(Long.parseLong(favoriteId), Long.parseLong(restaurantId));

        return new ResponseEntity<>(Map.of("message", "식당 삭제 성공"), org.springframework.http.HttpStatus.OK);
    }

}
