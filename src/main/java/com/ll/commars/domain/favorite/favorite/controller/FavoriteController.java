package com.ll.commars.domain.favorite.favorite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.favorite.favorite.dto.FavoriteCreateRequest;
import com.ll.commars.domain.favorite.favorite.dto.FavoriteCreateResponse;
import com.ll.commars.domain.favorite.favorite.dto.FavoriteFindListResponse;
import com.ll.commars.domain.favorite.favorite.dto.FavoriteRestaurantsResponse;
import com.ll.commars.domain.favorite.favorite.service.FavoriteService;
import com.ll.commars.global.security.annotation.AuthMemberId;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite")
public class FavoriteController {

	private final FavoriteService favoriteService;

	@PostMapping
	public ResponseEntity<FavoriteCreateResponse> createEmptyFavorite(
		@Valid @RequestBody FavoriteCreateRequest request,
		@AuthMemberId Long memberId
	) {
		FavoriteCreateResponse response = favoriteService.createFavorite(request, memberId);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping
	public ResponseEntity<FavoriteFindListResponse> getFavorites(
		@AuthMemberId Long memberId
	) {
		FavoriteFindListResponse response = favoriteService.getFavorites(memberId);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/{favorite-id}")
	public ResponseEntity<FavoriteRestaurantsResponse> getFavoriteRestaurants(
		@PathVariable("favorite-id") Long favoriteId,
		@AuthMemberId Long memberId
	) {
		FavoriteRestaurantsResponse response = favoriteService.getFavoriteRestaurants(favoriteId, memberId);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{favorite-id}")
	public ResponseEntity<Void> deleteFavorite(
		@PathVariable("favorite-id") Long favoriteId,
		@AuthMemberId Long memberId
	) {
		favoriteService.deleteFavorite(favoriteId, memberId);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{favorite-id}/restaurants/{restaurant-id}")
	public ResponseEntity<Void> deleteFavoriteRestaurant(
		@PathVariable("favorite-id") Long favoriteId,
		@PathVariable("restaurant-id") Long restaurantId,
		@AuthMemberId Long memberId
	) {
		favoriteService.deleteFavoriteRestaurant(favoriteId, restaurantId, memberId);
		return ResponseEntity.noContent().build();
	}
}
