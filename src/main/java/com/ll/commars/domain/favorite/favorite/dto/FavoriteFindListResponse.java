package com.ll.commars.domain.favorite.favorite.dto;

import java.util.List;

public record FavoriteFindListResponse(
	List<FavoriteFindResponse> favoriteList
) {

	public record FavoriteFindResponse (
		Long id,
		String name,
		boolean isPublic
	) {

	}
}
