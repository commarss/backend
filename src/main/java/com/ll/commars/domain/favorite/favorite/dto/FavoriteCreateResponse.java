package com.ll.commars.domain.favorite.favorite.dto;

import com.ll.commars.domain.favorite.favorite.entity.Favorite;

public record FavoriteCreateResponse(
	Long id,
	String name,
	boolean isPublic
) {

	public static FavoriteCreateResponse from(Favorite favorite) {
		return new FavoriteCreateResponse(
			favorite.getId(),
			favorite.getName(),
			favorite.isPublic()
		);
	}
}
